package com.nebrimatch.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.dto.ComunidadDTO;
import com.nebrimatch.backend.service.ComunidadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comunidades")
@RequiredArgsConstructor
public class ComunidadController {

    private final ComunidadService comunidadService;

    // GET /api/comunidades
    @GetMapping
    public ResponseEntity<List<ComunidadDTO>> obtenerTodas() {
        return ResponseEntity.ok(comunidadService.obtenerTodasLasComunidades());
    }

    // GET /api/comunidades/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ComunidadDTO> obtenerComunidadPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comunidadService.obtenerComunidadPorId(id));
    }

    // GET /api/comunidades/creador/{creadorId}
    @GetMapping("/creador/{creadorId}")
    public ResponseEntity<List<ComunidadDTO>> obtenerPorCreador(@PathVariable Long creadorId) {
        return ResponseEntity.ok(comunidadService.obtenerComunidadPorCreador(creadorId));
    }

    // GET /api/comunidades/buscar?texto=...
    @GetMapping("/buscar")
    public ResponseEntity<List<ComunidadDTO>> buscarComunidades(@RequestParam String texto) {
        return ResponseEntity.ok(comunidadService.buscarComunidades(texto));
    }

    // POST /api/comunidades
    @PostMapping
    public ResponseEntity<ComunidadDTO> crearComunidad(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam(required = false) String imagenComunidad,
            @RequestParam Long creadorId) {
        return ResponseEntity.status(201).body(
            comunidadService.crearComunidad(nombre, descripcion, imagenComunidad, creadorId)
        );
    }

    // PUT /api/comunidades/{idComunidad}
    @PutMapping("/{idComunidad}")
    public ResponseEntity<ComunidadDTO> actualizarComunidad(
            @PathVariable Long idComunidad,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam(required = false) String imagenComunidad,
            @RequestParam Long idSolicitante) {
        return ResponseEntity.ok(
            comunidadService.actualizarComunidad(idComunidad, nombre, descripcion, imagenComunidad, idSolicitante)
        );
    }

    // DELETE /api/comunidades/{idComunidad}
    @DeleteMapping("/{idComunidad}")
    public ResponseEntity<Void> eliminarComunidad(
            @PathVariable Long idComunidad,
            @RequestParam Long idSolicitante) {
        comunidadService.eliminarComunidad(idComunidad, idSolicitante);
        return ResponseEntity.noContent().build();
    }
}
