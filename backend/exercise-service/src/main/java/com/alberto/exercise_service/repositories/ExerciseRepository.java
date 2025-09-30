package com.alberto.exercise_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alberto.exercise_service.entities.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    
}
