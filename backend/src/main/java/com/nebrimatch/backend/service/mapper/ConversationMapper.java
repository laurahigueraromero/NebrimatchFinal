package com.nebrimatch.backend.service.mapper;

import org.springframework.stereotype.Component;

import com.nebrimatch.backend.dto.ConversacionDTO;
import com.nebrimatch.backend.model.Conversacion;

@Component
public class ConversationMapper {

    public ConversacionDTO toConversacionDTO(Conversacion c) {
        return new ConversacionDTO(
                c.getId(),
                c.getFechaCreacion(),
                c.getUsuario1().getId(),
                c.getUsuario1().getNombreUsuario(),
                c.getUsuario2().getId(),
                c.getUsuario2().getNombreUsuario()
        );
    }

}
