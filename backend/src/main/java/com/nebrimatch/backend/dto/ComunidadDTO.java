package com.nebrimatch.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComunidadDTO {

    private Long id;
    private String nombreComunidad;
    private String descripcionComunidad;
    private String imagenComunidad;
    private LocalDateTime fechaCreacion;
    private Long creadorId;
    private String creadorNombre;

}
