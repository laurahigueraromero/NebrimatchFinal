package com.nebrimatch.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nebrimatch.backend.dto.ConversacionDTO;
import com.nebrimatch.backend.dto.MensajeDTO;
import com.nebrimatch.backend.model.Conversacion;
import com.nebrimatch.backend.model.Mensaje;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.ConversacionRepository;
import com.nebrimatch.backend.repository.MensajeRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversacionService {

    private final ConversacionRepository conversacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MensajeRepository mensajeRepository;

    // crear conversacion ==>
    public ConversacionDTO crearConversacion(Long usuario1Id, Long usuario2Id) {

        Usuario usuario1 = usuarioRepository.findById(usuario1Id)
                .orElseThrow(() -> new RuntimeException("Usuario 1 no encontrado"));

        Usuario usuario2 = usuarioRepository.findById(usuario2Id)
                .orElseThrow(() -> new RuntimeException("Usuario 2 no encontrado"));

        boolean yaExiste = conversacionRepository
                .existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(
                        usuario1, usuario2, usuario1, usuario2);

        if (yaExiste) {
            throw new RuntimeException("Ya existe una conversación entre estos dos usuarios");
        }

        Conversacion conversacion = new Conversacion();
        conversacion.setUsuario1(usuario1);
        conversacion.setUsuario2(usuario2);

        Conversacion guardada = conversacionRepository.save(conversacion);

        return toConversacionDTO(guardada);
    }

    // obtener todas las conversaciones de un usuario ==>
    public List<ConversacionDTO> obtenerConversacionesPorUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return conversacionRepository.findByUsuario1OrUsuario2(usuario, usuario)
                .stream()
                .map(this::toConversacionDTO)
                .collect(Collectors.toList());
    }

    // obtener una conversación por su ID ==>
    public ConversacionDTO obtenerConversacionPorId(Long conversacionId) {

        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

        return toConversacionDTO(conversacion);
    }

    // enviar un mensaje en una conversación ==>
    public MensajeDTO enviarMensaje(Long conversacionId, Long emisorId, String contenido) {

        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

        Usuario emisor = usuarioRepository.findById(emisorId)
                .orElseThrow(() -> new RuntimeException("Emisor no encontrado"));

        Mensaje mensaje = new Mensaje();
        mensaje.setConversacion(conversacion);
        mensaje.setEmisor(emisor);
        mensaje.setContenido(contenido);

        Mensaje guardado = mensajeRepository.save(mensaje);

        return toMensajeDTO(guardado);
    }

    // obtener todos los mensajes de una conversación en orden cronológico ==>
    public List<MensajeDTO> obtenerMensajes(Long conversacionId) {

        return mensajeRepository.findByConversacionIdOrderByFechaEnvioAsc(conversacionId)
                .stream()
                .map(this::toMensajeDTO)
                .collect(Collectors.toList());
    }

    // obtener el último mensaje de una conversación (para previsualización) ==>
    public MensajeDTO obtenerUltimoMensaje(Long conversacionId) {

        return mensajeRepository.findTop1ByConversacionIdOrderByFechaEnvioDesc(conversacionId)
                .stream()
                .findFirst()
                .map(this::toMensajeDTO)
                .orElse(null);
    }

    // mappers privados ==>
    private ConversacionDTO toConversacionDTO(Conversacion c) {
        return new ConversacionDTO(
                c.getId(),
                c.getFechaCreacion(),
                c.getUsuario1().getId(),
                c.getUsuario1().getNombreUsuario(),
                c.getUsuario2().getId(),
                c.getUsuario2().getNombreUsuario()
        );
    }

    private MensajeDTO toMensajeDTO(Mensaje m) {
        return new MensajeDTO(
                m.getId(),
                m.getContenido(),
                m.getFechaEnvio(),
                m.getEmisor().getId(),
                m.getEmisor().getNombreUsuario(),
                m.getConversacion().getId()
        );
    }
}
