package com.nebrimatch.backend.service.mapper;

import org.springframework.stereotype.Component;

import com.nebrimatch.backend.dto.MensajeDTO;
import com.nebrimatch.backend.model.Mensaje;

@Component
public class MessageMapper {

    public MensajeDTO toMensajeDTO(Mensaje m) {
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
