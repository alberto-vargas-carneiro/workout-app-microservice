package com.alberto.workout_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alberto.workout_service.entities.WorkoutItem;

@Repository
public interface WorkoutItemRepository extends JpaRepository<WorkoutItem, Long> {
    
}
