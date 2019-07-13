package com.exercise.toggler.repository;

import com.exercise.toggler.model.Toggle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToggleRepository extends JpaRepository<Toggle, String> {
}
