package com.nebrimatch.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.model.Lenguaje;
import com.nebrimatch.backend.repository.LenguajeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lenguajes")
@RequiredArgsConstructor
public class LenguajeController {

    private final LenguajeRepository lenguajeRepository;

    @GetMapping
    public ResponseEntity<List<String>> obtenerTodos() {
        List<String> nombres = lenguajeRepository.findAll()
                .stream()
                .map(Lenguaje::getNombre)
                .collect(Collectors.toList());
        return ResponseEntity.ok(nombres);
    }
}
