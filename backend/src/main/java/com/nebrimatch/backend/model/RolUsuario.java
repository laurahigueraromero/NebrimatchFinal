package com.nebrimatch.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rol_usuario")
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // estrategia de generación de IDs en JPA/Hibernate.
    private Long id;

    @Column(name = "rol", nullable = false)
    // le dice a JPA cómo guardar un enum de Java en la base de datos, guarda "estudiante" o "profesor"
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // OneToOne==> un usuario tiene exactamente un rol, y un rol pertenece a exactamente un usuario.
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    public enum Rol {
        estudiante, profesor
    }
}
