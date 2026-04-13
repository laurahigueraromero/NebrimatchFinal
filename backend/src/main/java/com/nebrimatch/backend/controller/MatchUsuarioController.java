package com.nebrimatch.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.dto.MatchUsuarioDTO;
import com.nebrimatch.backend.service.MatchUsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchUsuarioController {

    private final MatchUsuarioService matchUsuarioService;

    // Crear un match
    @PostMapping
    public ResponseEntity<MatchUsuarioDTO> crearMatch(
            @RequestParam Long usuario1Id,
            @RequestParam Long usuario2Id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchUsuarioService.crearMatch(usuario1Id, usuario2Id));
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<MatchUsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matchUsuarioService.obtenerMatchPorId(id));
    }

    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MatchUsuarioDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(matchUsuarioService.obtenerMatchesPorUsuario(usuarioId));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        matchUsuarioService.eliminarMatch(id);
        return ResponseEntity.noContent().build();
    }
}
