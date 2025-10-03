package com.alberto.workout_service.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.alberto.workout_service.entities.Workout;
import com.alberto.workout_service.entities.WorkoutItem;

import jakarta.validation.constraints.NotEmpty;

public class WorkoutDTO {
    
    private Long id;
    private String name;
    private Instant date;

    @NotEmpty(message = "Deve ter pelo menos um exercício")
    private List<WorkoutItemDTO> workoutItems = new ArrayList<>();

    public WorkoutDTO() {
    }

    public WorkoutDTO(Long id, String name, Instant date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
    
    public WorkoutDTO(Workout entity) {
        id = entity.getId();
        name = entity.getName();
        date = entity.getDate();
        for (WorkoutItem workoutItem : entity.getWorkoutItem()) {
			WorkoutItemDTO workoutItemDTO = new WorkoutItemDTO(workoutItem);
			workoutItems.add(workoutItemDTO);
		};
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getDate() {
        return date;
    }
    
    public List<WorkoutItemDTO> getWorkoutItems() {
        return workoutItems;
    }
}
