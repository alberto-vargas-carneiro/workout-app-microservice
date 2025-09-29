package com.alberto.exercise_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alberto.exercise_service.dto.ExerciseDTO;
import com.alberto.exercise_service.services.ExerciseService;

@RestController
@RequestMapping(value = "/exercises")
public class ExerciseController {
    
    @Autowired
    private ExerciseService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping
    public ResponseEntity<Page<ExerciseDTO>> findAll(Pageable pageable) {
        Page<ExerciseDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ExerciseDTO> findById(@PathVariable Long id) {
        ExerciseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }
}
