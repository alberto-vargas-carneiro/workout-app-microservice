package com.alberto.workout_service.dto;

import com.alberto.workout_service.entities.WorkoutItem;

public class WorkoutItemDTO {

    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private Integer setNumber;
    private String reps;
    private Integer rest;
    private Integer weight;
    private String image;

    public WorkoutItemDTO() {
    }

    public WorkoutItemDTO(Long id, Long exerciseId, String exerciseName, Integer setNumber, String reps,
            Integer rest, Integer weight, String image) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.reps = reps;
        this.rest = rest;
        this.weight = weight;
        this.image = image;
    }

    public WorkoutItemDTO(WorkoutItem entity) {
        id = entity.getId();
        exerciseId = entity.getExerciseId();
        setNumber = entity.getSetNumber();
        reps = entity.getReps();
        rest = entity.getRest();
        weight = entity.getWeight();
    }

    public Long getId() {
        return id;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getSetNumber() {
        return setNumber;
    }

    public String getReps() {
        return reps;
    }

    public Integer getRest() {
        return rest;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
