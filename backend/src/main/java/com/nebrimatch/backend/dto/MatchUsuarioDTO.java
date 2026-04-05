package com.nebrimatch.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchUsuarioDTO {

    private Long id;
    private Long usuario1Id;
    private String usuario1Nombre;
    private Long usuario2Id;
    private String usuario2Nombre;
    private LocalDateTime fechaMatch;

}
