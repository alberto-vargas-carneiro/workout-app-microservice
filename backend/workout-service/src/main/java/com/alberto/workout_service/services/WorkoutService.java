package com.alberto.workout_service.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alberto.workout_service.dto.ExerciseDTO;
import com.alberto.workout_service.clients.ExerciseClient;
import com.alberto.workout_service.dto.WorkoutDTO;
import com.alberto.workout_service.dto.WorkoutItemDTO;
import com.alberto.workout_service.dto.WorkoutMinDTO;
import com.alberto.workout_service.entities.Workout;
import com.alberto.workout_service.entities.WorkoutItem;
import com.alberto.workout_service.repositories.WorkoutItemRepository;
import com.alberto.workout_service.repositories.WorkoutRepository;
import com.alberto.workout_service.services.exceptions.DatabaseException;
import com.alberto.workout_service.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository repository;

    @Autowired
    private WorkoutItemRepository workoutItemRepository;

    @Autowired
    private ExerciseClient exerciseClient;

    @Autowired
    private ValidateUser validateUser;

    @Transactional(readOnly = true)
    public WorkoutDTO findById(Long id) {
        Workout workout = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        validateUser.validateSelfOrAdmin(workout.getUserId());
        
        WorkoutDTO resultDto = new WorkoutDTO(workout);
        for (WorkoutItemDTO itemDto : resultDto.getWorkoutItems()) {
            try {
                ExerciseDTO exercise = exerciseClient.findById(itemDto.getExerciseId());
                itemDto.setExerciseName(exercise.getName());
                itemDto.setImage(exercise.getImage());
            } catch (Exception e) {
                System.err.println("Erro ao buscar exercício " + itemDto.getExerciseId() + ": " + e.getMessage());
            }
        }
        return resultDto;
    }

    @Transactional(readOnly = true)
    public List<WorkoutMinDTO> findByUserId(Long userId) {
        List<Workout> workouts = repository.findByUserId(userId);
        validateUser.validateSelfOrAdmin(userId);
        return workouts.stream().map(WorkoutMinDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public WorkoutDTO insert(WorkoutDTO dto) {
        Workout entity = new Workout();
        entity.setName(dto.getName());
        entity.setDate(Instant.now());
        Long userId = SecurityUtils.getAuthenticatedUserId();
        entity.setUserId(userId);
        
        for (WorkoutItemDTO workoutItemDto : dto.getWorkoutItems()) {
            WorkoutItem workoutItem = new WorkoutItem(
                    workoutItemDto.getId(),
                    entity,
                    workoutItemDto.getExerciseId(),
                    workoutItemDto.getSetNumber(),
                    workoutItemDto.getReps(),
                    workoutItemDto.getRest(),
                    workoutItemDto.getWeight());
            entity.getWorkoutItem().add(workoutItem);
        }
        
        // Salvar apenas a entidade principal - o cascade cuida dos itens
        entity = repository.save(entity);
        
        WorkoutDTO resultDto = new WorkoutDTO(entity);
        for (WorkoutItemDTO itemDto : resultDto.getWorkoutItems()) {
            try {
                ExerciseDTO exercise = exerciseClient.findById(itemDto.getExerciseId());
                itemDto.setExerciseName(exercise.getName());
                itemDto.setImage(exercise.getImage());
            } catch (Exception e) {
                System.err.println("Erro ao buscar exercício " + itemDto.getExerciseId() + ": " + e.getMessage());
            }
        }
        return resultDto;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade");
        }
    }
    
    @Transactional
    public WorkoutDTO update(Long id, WorkoutDTO dto) {
        try {
            Workout entity = repository.getReferenceById(id);
            validateUser.validateSelfOrAdmin(entity.getUserId());

            entity.setName(dto.getName());

            // Criar mapa dos itens existentes
            Map<Long, WorkoutItem> existingItemsMap = entity.getWorkoutItem().stream()
                    .collect(Collectors.toMap(WorkoutItem::getId, item -> item));

            // Lista para os itens que serão mantidos/atualizados
            List<WorkoutItem> updatedItems = new ArrayList<>();
            
            for (WorkoutItemDTO itemDto : dto.getWorkoutItems()) {
                if (itemDto.getExerciseId() == null) {
                    throw new IllegalArgumentException("O campo exerciseId é obrigatório.");
                }

                WorkoutItem item;

                // Se é um item existente, atualiza
                if (itemDto.getId() != null && existingItemsMap.containsKey(itemDto.getId())) {
                    item = existingItemsMap.remove(itemDto.getId());
                    item.setExerciseId(itemDto.getExerciseId());
                    item.setSetNumber(itemDto.getSetNumber());
                    item.setReps(itemDto.getReps());
                    item.setRest(itemDto.getRest());
                    item.setWeight(itemDto.getWeight());
                } else {
                    // Se é um item novo, cria
                    item = new WorkoutItem();
                    item.setWorkout(entity);
                    item.setExerciseId(itemDto.getExerciseId());
                    item.setSetNumber(itemDto.getSetNumber());
                    item.setReps(itemDto.getReps());
                    item.setRest(itemDto.getRest());
                    item.setWeight(itemDto.getWeight());
                }

                updatedItems.add(item);
            }
            
            // Remover itens que não estão mais na lista (foram excluídos)
            for (WorkoutItem toRemove : existingItemsMap.values()) {
                entity.getWorkoutItem().remove(toRemove);
                workoutItemRepository.delete(toRemove);
            }
            
            // Limpar e adicionar todos os itens atualizados
            entity.getWorkoutItem().clear();
            entity.getWorkoutItem().addAll(updatedItems);

            // Salvar apenas a entidade principal - o cascade cuida dos itens
            entity = repository.save(entity);
            
            WorkoutDTO resultDto = new WorkoutDTO(entity);
            for (WorkoutItemDTO itemDto : resultDto.getWorkoutItems()) {
                try {
                    ExerciseDTO exercise = exerciseClient.findById(itemDto.getExerciseId());
                    itemDto.setExerciseName(exercise.getName());
                    itemDto.setImage(exercise.getImage());
                } catch (Exception e) {
                    System.err.println("Erro ao buscar exercício " + itemDto.getExerciseId() + ": " + e.getMessage());
                }
            }
            return resultDto;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Workout não encontrado");
        }
    }
}
