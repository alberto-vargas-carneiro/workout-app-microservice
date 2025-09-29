package com.alberto.workout_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alberto.workout_service.entities.Workout;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

}
