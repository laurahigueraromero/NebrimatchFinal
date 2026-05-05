package com.nebrimatch.backend.service;

import org.springframework.stereotype.Service;

import com.nebrimatch.backend.model.Comunidad;
import com.nebrimatch.backend.model.MiembroComunidad;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.ComunidadRepository;
import com.nebrimatch.backend.repository.MiembroComunidadRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MiembroComunidadService {

    private final MiembroComunidadRepository miembroRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComunidadRepository comunidadRepository;

    public void unirse(Long comunidadId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Comunidad comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        if (miembroRepository.existsByUsuarioAndComunidad(usuario, comunidad)) {
            throw new RuntimeException("Ya eres miembro de esta comunidad");
        }

        MiembroComunidad miembro = new MiembroComunidad();
        miembro.setUsuario(usuario);
        miembro.setComunidad(comunidad);
        miembroRepository.save(miembro);
    }

    public void salir(Long comunidadId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Comunidad comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        MiembroComunidad miembro = miembroRepository.findByUsuarioAndComunidad(usuario, comunidad)
                .orElseThrow(() -> new RuntimeException("No eres miembro de esta comunidad"));

        miembroRepository.delete(miembro);
    }

    public boolean esMiembro(Long comunidadId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Comunidad comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        return miembroRepository.existsByUsuarioAndComunidad(usuario, comunidad);
    }

    public long contarMiembros(Long comunidadId) {
        Comunidad comunidad = comunidadRepository.findById(comunidadId)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        return miembroRepository.countByComunidad(comunidad);
    }
}
