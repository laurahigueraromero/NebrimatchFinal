package com.nebrimatch.backend.service.mapper;

import org.springframework.stereotype.Component;

import com.nebrimatch.backend.dto.MatchUsuarioDTO;
import com.nebrimatch.backend.model.MatchUsuario;

@Component
public class MatchMapper {

    public MatchUsuarioDTO toMatchUsuarioDTO(MatchUsuario m) {
        return new MatchUsuarioDTO(
                m.getId(),
                m.getUsuario1().getId(),
                m.getUsuario1().getNombreUsuario(),
                m.getUsuario2().getId(),
                m.getUsuario2().getNombreUsuario(),
                m.getFechaMatch()
        );
    }

}
