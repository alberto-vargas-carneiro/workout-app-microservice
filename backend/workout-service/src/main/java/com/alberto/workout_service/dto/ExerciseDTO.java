package com.alberto.workout_service.dto;

public class ExerciseDTO {

    private String name;
    private String image;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
