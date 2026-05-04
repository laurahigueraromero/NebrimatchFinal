package com.nebrimatch.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// las tres primeras anotaciones generan (data=> los getter y setters), un constructor vacío y constructor con todos los campos
// de esta manera ahorramos código
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "lenguajes_a_ensenar", length = 200)
    private String lenguajesAEnsenar;

    @Column(name = "lenguajes_a_aprender", length = 200)
    private String lenguajesAAprender;

    @Column(name = "numero_matches")
    private Integer numeroMatches = 0;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private RolUsuario rolUsuario;

    // el método que se ejecuta automáticamente justo antes de que el objeto se guarde en la base de datos por primera vez con PrePersist==>
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}