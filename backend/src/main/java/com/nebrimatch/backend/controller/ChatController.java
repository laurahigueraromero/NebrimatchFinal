package com.nebrimatch.backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.nebrimatch.backend.dto.MensajeDTO;
import com.nebrimatch.backend.service.ConversacionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ConversacionService conversacionService;

    // El cliente envía a: /app/chat.enviar/{conversacionId}
    // El service guarda el mensaje y lo publica en: /topic/conversations/{conversacionId}
    @MessageMapping("/chat.enviar/{conversacionId}")
    public MensajeDTO enviarMensaje(
            @DestinationVariable Long conversacionId,
            @Payload MensajeDTO mensajeDTO) {
        return conversacionService.enviarMensaje(
                conversacionId,
                mensajeDTO.getEmisorId(),
                mensajeDTO.getContenido()
        );
    }
}
// El ChatController ya está completo. Solo necesita el @MessageMapping de enviar mensaje porque es el único flujo en tiempo real.
