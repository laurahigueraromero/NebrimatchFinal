package com.nebrimatch.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nebrimatch.backend.dto.ConversacionDTO;
import com.nebrimatch.backend.dto.MensajeDTO;
import com.nebrimatch.backend.exception.BadRequestException;
import com.nebrimatch.backend.exception.ConflictException;
import com.nebrimatch.backend.exception.NotFoundException;
import com.nebrimatch.backend.model.Conversacion;
import com.nebrimatch.backend.model.Mensaje;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.ConversacionRepository;
import com.nebrimatch.backend.repository.MensajeRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;
import com.nebrimatch.backend.service.mapper.ConversationMapper;
import com.nebrimatch.backend.service.mapper.MessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversacionService {

        private final ConversacionRepository conversacionRepository;
        private final UsuarioRepository usuarioRepository;
        private final MensajeRepository mensajeRepository;
        private final ConversationMapper conversationMapper;
        private final MessageMapper messageMapper;
        private final SimpMessagingTemplate messagingTemplate;
                // clase de Spring que te permite enviar mensajes WebSocket desde el servidor hacia los clientes.
        @Transactional
        // esto es para que se ejecute todo de una, osea si algo falla no ejecuta nada
        public ConversacionDTO crearConversacion(Long usuario1Id, Long usuario2Id) {

                Usuario usuario1 = usuarioRepository.findById(usuario1Id)
                                .orElseThrow(() -> new NotFoundException("Usuario 1 no encontrado"));

                Usuario usuario2 = usuarioRepository.findById(usuario2Id)
                                .orElseThrow(() -> new NotFoundException("Usuario 2 no encontrado"));

                boolean yaExiste = conversacionRepository
                                .existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(
                                                usuario1, usuario2, usuario1, usuario2);

                if (yaExiste) {
                        throw new ConflictException("Ya existe una conversación entre estos dos usuarios");
                }

                Conversacion conversacion = new Conversacion();
                conversacion.setUsuario1(usuario1);
                conversacion.setUsuario2(usuario2);

                Conversacion guardada = conversacionRepository.save(conversacion);

                log.info("Conversación creada: {} entre {} y {}", guardada.getId(), usuario1Id, usuario2Id);

                return conversationMapper.toConversacionDTO(guardada);
        }

        public List<ConversacionDTO> obtenerConversacionesPorUsuario(Long usuarioId) {

                Usuario usuario = usuarioRepository.findById(usuarioId)
                                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                return conversacionRepository.findByUsuario1OrUsuario2(usuario, usuario)
                                .stream()
                                .map(conversationMapper::toConversacionDTO)
                                .collect(Collectors.toList());
                                // esto empuja datos del servidor al cliente sin que el cliente lo pida
        }

        public ConversacionDTO obtenerConversacionPorId(Long conversacionId) {

                Conversacion conversacion = conversacionRepository.findById(conversacionId)
                                .orElseThrow(() -> new NotFoundException("Conversación no encontrada"));

                return conversationMapper.toConversacionDTO(conversacion);
        }

        @Transactional
        public MensajeDTO enviarMensaje(Long conversacionId, Long emisorId, String contenido) {

                if (contenido == null || contenido.trim().isEmpty()) {
                        throw new BadRequestException("Contenido del mensaje no puede estar vacío");
                }

                Conversacion conversacion = conversacionRepository.findById(conversacionId)
                                .orElseThrow(() -> new NotFoundException("Conversación no encontrada"));

                Usuario emisor = usuarioRepository.findById(emisorId)
                                .orElseThrow(() -> new NotFoundException("Emisor no encontrado"));

                // permiso: el emisor debe formar parte de la conversación
                if (!emisor.equals(conversacion.getUsuario1()) && !emisor.equals(conversacion.getUsuario2())) {
                        throw new ConflictException("El usuario no pertenece a esta conversación");
                }

                Mensaje mensaje = new Mensaje();
                mensaje.setConversacion(conversacion);
                mensaje.setEmisor(emisor);
                mensaje.setContenido(contenido);

                Mensaje guardado = mensajeRepository.save(mensaje);

                MensajeDTO dto = messageMapper.toMensajeDTO(guardado);

                // publicar por WebSocket para notificar a suscriptores es decir, enviar el mensaje en tiempo real
                try {
                        messagingTemplate.convertAndSend("/topic/conversations/" + conversacionId, dto);
                } catch (Exception ex) {
                        log.warn("No se pudo enviar notificación WebSocket para conversacion {}: {}", conversacionId, ex.getMessage());
                }

                return dto;
        }

        public List<MensajeDTO> obtenerMensajes(Long conversacionId) {

                return mensajeRepository.findByConversacionIdOrderByFechaEnvioAsc(conversacionId)
                                .stream()
                                .map(messageMapper::toMensajeDTO)
                                .collect(Collectors.toList());
        }

        public MensajeDTO obtenerUltimoMensaje(Long conversacionId) {

                return mensajeRepository.findTopByConversacionIdOrderByFechaEnvioDesc(conversacionId)
                                .map(messageMapper::toMensajeDTO)
                                .orElse(null);
        }
}
