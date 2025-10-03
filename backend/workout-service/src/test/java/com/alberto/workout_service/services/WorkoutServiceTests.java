package com.alberto.workout_service.services;

import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alberto.workout_service.clients.ExerciseClient;
import com.alberto.workout_service.dto.ExerciseDTO;
import com.alberto.workout_service.dto.WorkoutDTO;
import com.alberto.workout_service.entities.Workout;
import com.alberto.workout_service.entities.WorkoutItem;
import com.alberto.workout_service.repositories.WorkoutItemRepository;
import com.alberto.workout_service.repositories.WorkoutRepository;
import com.alberto.workout_service.services.exceptions.ForbiddenException;
import com.alberto.workout_service.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class WorkoutServiceTests {

    @InjectMocks
    private WorkoutService workoutService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutItemRepository workoutItemRepository;

    @Mock
    private ValidateUser validateUser;

    @Mock
    private ExerciseClient exerciseClient;

    private Workout workout;
    private WorkoutDTO workoutDTO;
    private Long userId;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        workout = new Workout(1L, "Morning Workout", Instant.now(), userId);

        Mockito.when(workoutRepository.findById(1L)).thenReturn(Optional.of(workout));
    }

    @Test
    public void findById_ShouldReturnWorkoutDTO_WhenWorkoutExistsAndClientLogged() {

        Mockito.doNothing().when(validateUser).validateSelfOrAdmin(any());

        WorkoutDTO result = workoutService.findById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(workout.getId(), result.getId());
        Assertions.assertEquals(workout.getName(), result.getName());
        Assertions.assertEquals(workout.getDate(), result.getDate());
    }

    @Test
    public void findById_ShouldThrowForbiddenException_WhenIdExistsAndOtherClientLogged() {

        Mockito.doThrow(ForbiddenException.class).when(validateUser).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            @SuppressWarnings("unused")
            WorkoutDTO result = workoutService.findById(1L);
        });
    }

    @Test
    public void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExist() {

        Mockito.doNothing().when(validateUser).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            @SuppressWarnings("unused")
            WorkoutDTO result = workoutService.findById(2L);
        });
    }

    @Test
    public void insert_ShouldInsertWorkout_WhenValidData() {

        ExerciseDTO exerciseDTO = new ExerciseDTO("Push Up", "pushup.mp4");
        WorkoutItem workoutItem = new WorkoutItem(null, workout, 1L, 1, "3", 10, 10);
        workout.getWorkoutItem().add(workoutItem);
        workoutDTO = new WorkoutDTO(workout);

        try (var mockedSecurityUtils = Mockito.mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUserId).thenReturn(userId);
            Mockito.when(exerciseClient.findById(1L)).thenReturn(exerciseDTO);
            Mockito.when(workoutRepository.save(any(Workout.class))).thenReturn(workout);

            WorkoutDTO result = workoutService.insert(workoutDTO);

            Assertions.assertNotNull(result);
            Assertions.assertEquals(workoutDTO.getName(), result.getName());
            Assertions.assertEquals(userId, workout.getUserId());
        }
    }
}
