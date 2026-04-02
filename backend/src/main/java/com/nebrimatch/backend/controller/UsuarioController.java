package com.nebrimatch.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nebrimatch.backend.dto.UsuarioDTO;
import com.nebrimatch.backend.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
// Define esta clase como un controlador REST, lo que permite manejar solicitudes HTTP y devolver respuestas en formato JSON.
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
// Es equivalente a poner @Autowired en el constructor, pero sin escribirlo manualmente;
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    // GET /api/usuarios/email/{email}
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(usuarioService.obtenerPorEmail(email));
    }

    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<UsuarioDTO> crear(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(id, dto));
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/usuarios/matching?ensenan=Java&aprenden=Python
    @GetMapping("/matching")
    public ResponseEntity<List<UsuarioDTO>> buscarParaMatching(
            @RequestParam(required = false) String ensenan,
            @RequestParam(required = false) String aprenden) {
        if (ensenan != null) {
            return ResponseEntity.ok(usuarioService.buscarPorLenguajeEnsenado(ensenan));
        }
        if (aprenden != null) {
            return ResponseEntity.ok(usuarioService.buscarPorLenguajeAprendido(aprenden));
        }
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
}
