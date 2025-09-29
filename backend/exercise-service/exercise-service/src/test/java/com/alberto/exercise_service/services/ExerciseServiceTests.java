package com.alberto.exercise_service.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alberto.exercise_service.dto.ExerciseDTO;
import com.alberto.exercise_service.entities.Exercise;
import com.alberto.exercise_service.repositories.ExerciseRepository;

@ExtendWith(SpringExtension.class)
public class ExerciseServiceTests {

    @InjectMocks
    private ExerciseService exerciseService;

    @Mock
    private ExerciseRepository exerciseRepository;

    private Exercise exercise;
    private PageImpl<Exercise> page;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise(1L, "Push Up", "push_up_image.mp4");
        page = new PageImpl<>(List.of(exercise));

        Mockito.when(exerciseRepository.findAll(any(Pageable.class))).thenReturn(page);
    }

    @Test
    public void findAll_ShouldReturnAllExercises() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ExerciseDTO> result = exerciseService.findAll(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(exercise.getName(), result.getContent().get(0).getName());
        Assertions.assertEquals(exercise.getImage(), result.getContent().get(0).getImage());
        Assertions.assertEquals(1L, result.getContent().get(0).getId());
    }
}
