package com.nebrimatch.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nebrimatch.backend.model.Lenguaje;

public interface LenguajeRepository extends JpaRepository<Lenguaje, Long> {
    boolean existsByNombre(String nombre);
}
