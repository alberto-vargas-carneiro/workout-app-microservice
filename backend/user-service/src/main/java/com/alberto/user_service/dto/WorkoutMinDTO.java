package com.alberto.user_service.dto;

import java.time.Instant;

public class WorkoutMinDTO {

    private Long id;
    private String name;
    private Instant date;

    public WorkoutMinDTO() {
    }

    public WorkoutMinDTO(Long id, String name, Instant date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }
    
    public void setDate(Instant date) {
        this.date = date;
    }
    
}