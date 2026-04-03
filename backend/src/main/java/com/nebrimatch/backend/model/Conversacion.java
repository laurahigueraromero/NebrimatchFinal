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
@Table(name = "conversacion")
public class Conversacion {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "fecha_creacion", updatable = false)
private LocalDateTime fechaCreacion;

// los dos usuarios que participan en la conversacion ==>
@ManyToOne
@JoinColumn(name = "usuario1_id", nullable = false)
private Usuario usuario1;

@ManyToOne
@JoinColumn(name = "usuario2_id", nullable = false)
private Usuario usuario2;


// @PrePersist es un método que Spring ejecuta automáticamente justo antes de guardar el objeto en la base de datos por primera vez.
// esto hace que cada vez que se cree una nueva conversación, la fecha de creación se establezca automáticamente en el momento de su creación, sin necesidad de que el desarrollador lo haga manualmente
@PrePersist
protected void onCreate() {
    fechaCreacion = LocalDateTime.now();
}

}
