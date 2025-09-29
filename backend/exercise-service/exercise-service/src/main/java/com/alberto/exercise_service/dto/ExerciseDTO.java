package com.alberto.exercise_service.dto;

import com.alberto.exercise_service.entities.Exercise;

public class ExerciseDTO {
    
    private Long id;
    private String name;
    private String image;

    public ExerciseDTO(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    public ExerciseDTO(Exercise entity) {
        id = entity.getId();
        name = entity.getName();
        image = entity.getImage();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
