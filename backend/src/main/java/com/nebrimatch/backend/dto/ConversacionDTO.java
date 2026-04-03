package com.nebrimatch.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversacionDTO {

    private Long id;
    private LocalDateTime fechaCreacion;
    private Long usuario1Id;
    private String usuario1Nombre;
    private Long usuario2Id;
    private String usuario2Nombre;

}
