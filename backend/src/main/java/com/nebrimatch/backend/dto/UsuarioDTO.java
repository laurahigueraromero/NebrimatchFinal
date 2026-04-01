package com.nebrimatch.backend.dto;

import java.time.LocalDateTime;

import com.nebrimatch.backend.model.RolUsuario.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nombreUsuario;
    private String email;
    private String descripcion;
    private String lenguajesAEnsenar;
    private String lenguajesAAprender;
    private Integer numeroMatches;
    private LocalDateTime fechaCreacion;
    private Rol rol;
}
