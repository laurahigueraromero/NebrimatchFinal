package com.nebrimatch.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.dto.LoginRequestDTO;
import com.nebrimatch.backend.dto.UsuarioDTO;
import com.nebrimatch.backend.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
}
