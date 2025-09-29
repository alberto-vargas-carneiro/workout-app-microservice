package com.alberto.workout_service.dto;

import java.time.Instant;

import com.alberto.workout_service.entities.Workout;

public class WorkoutMinDTO {

    private Long id;
    private String name;
    private Instant date;

    public WorkoutMinDTO(Long id, String name, Instant date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public WorkoutMinDTO(Workout entity) {
        id = entity.getId();
        name = entity.getName();
        date = entity.getDate();
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
}
