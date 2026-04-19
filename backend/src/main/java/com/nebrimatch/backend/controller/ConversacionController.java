package com.nebrimatch.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.dto.ConversacionDTO;
import com.nebrimatch.backend.dto.MensajeDTO;
import com.nebrimatch.backend.service.ConversacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {

    private final ConversacionService conversacionService;

    // POST /api/conversaciones?usuario1Id=1&usuario2Id=2
    @PostMapping
    public ResponseEntity<ConversacionDTO> crearConversacion(
            @RequestParam Long usuario1Id,
            @RequestParam Long usuario2Id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversacionService.crearConversacion(usuario1Id, usuario2Id));
    }

    // GET /api/conversaciones/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ConversacionDTO>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(conversacionService.obtenerConversacionesPorUsuario(usuarioId));
    }

    // GET /api/conversaciones/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ConversacionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(conversacionService.obtenerConversacionPorId(id));
    }

    // GET /api/conversaciones/{id}/mensajes
    @GetMapping("/{id}/mensajes")
    public ResponseEntity<List<MensajeDTO>> obtenerMensajes(@PathVariable Long id) {
        return ResponseEntity.ok(conversacionService.obtenerMensajes(id));
    }

    // GET /api/conversaciones/{id}/mensajes/ultimo
    @GetMapping("/{id}/mensajes/ultimo")
    public ResponseEntity<MensajeDTO> obtenerUltimoMensaje(@PathVariable Long id) {
        return ResponseEntity.ok(conversacionService.obtenerUltimoMensaje(id));
    }
}
