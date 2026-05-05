package com.nebrimatch.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.service.MiembroComunidadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comunidades")
@RequiredArgsConstructor
public class MiembroComunidadController {

    private final MiembroComunidadService miembroService;

    @PostMapping("/{id}/unirse")
    public ResponseEntity<String> unirse(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        miembroService.unirse(id, usuarioId);
        return ResponseEntity.ok("Te has unido a la comunidad");
    }

    @DeleteMapping("/{id}/salir")
    public ResponseEntity<String> salir(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        miembroService.salir(id, usuarioId);
        return ResponseEntity.ok("Has salido de la comunidad");
    }

    @GetMapping("/{id}/esMiembro")
    public ResponseEntity<Boolean> esMiembro(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(miembroService.esMiembro(id, usuarioId));
    }

    @GetMapping("/{id}/miembros/count")
    public ResponseEntity<Long> contarMiembros(@PathVariable Long id) {
        return ResponseEntity.ok(miembroService.contarMiembros(id));
    }
}
