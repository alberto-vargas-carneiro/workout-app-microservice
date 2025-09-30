package com.alberto.exercise_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alberto.exercise_service.dto.ExerciseDTO;
import com.alberto.exercise_service.entities.Exercise;
import com.alberto.exercise_service.repositories.ExerciseRepository;
import com.alberto.exercise_service.services.exceptions.ResourceNotFoundException;

@Service
public class ExerciseService {
    
    @Autowired
    private ExerciseRepository repository;

    @Transactional(readOnly = true)
    public Page<ExerciseDTO> findAll(Pageable pageable) {
        Page<Exercise> result = repository.findAll(pageable);
        return result.map(x -> new ExerciseDTO(x));
    }

    @Transactional(readOnly = true)
    public ExerciseDTO findById(Long id) {
        Exercise exercise = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ExerciseDTO(exercise);
    }
}
