package com.nebrimatch.backend.model;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comunidad")
public class Comunidad {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "nombre_comunidad", nullable = false, unique = true, length = 100)
private String nombreComunidad;

@Column(name= "descripcion_comunidad", columnDefinition = "TEXT", nullable = false)
private String descripcionComunidad;

@Column (name="imagen_comunidad")
private String imagenComunidad;

@Column (name= "fecha_creacion", updatable = false)
private LocalDateTime fechaCreacion;


//foreign key ==>
@ManyToOne
@JoinColumn(name = "creador_comunidad", nullable = false)
private Usuario creadorComunidad;

/*@PrePersist es un hook de JPA que ejecuta un método justo antes de que la entidad se guarde por primera vez en la base de datos. */

@PrePersist
protected void onCreate() {
    fechaCreacion = LocalDateTime.now();
}
}
