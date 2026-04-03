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
@Table(name = "mensaje")
public class Mensaje {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(name = "contenido", columnDefinition = "TEXT", nullable = false)
private String contenido;

@Column(name = "fecha_envio", updatable = false)
private LocalDateTime fechaEnvio;

//foreign keys ==>
@ManyToOne
@JoinColumn(name = "emisor_id", nullable = false)
private Usuario emisor;

@ManyToOne
@JoinColumn(name = "conversacion_id", nullable = false)
private Conversacion conversacion;

@PrePersist
protected void onCreate() {
    fechaEnvio = LocalDateTime.now();
}

// @PrePersist es un método que Spring ejecuta automáticamente justo antes de guardar el objeto en la base de datos por primera vez.
// esto hace que cada vez que se cree un nuevo mensaje, la fecha de envío se establezca automáticamente en el momento de su creación, sin necesidad de que el desarrollador lo haga manualmente.

}
