package com.nebrimatch.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nebrimatch.backend.model.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // buscar mensajes por conversacion
    List<Mensaje> findByConversacionId(Long conversacionId);

    // mensajes ordenados por fecha (para mostrar el chat en orden cronológico)
    List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Long conversacionId);

    // último mensaje de una conversación (previsualización)
    java.util.Optional<Mensaje> findTopByConversacionIdOrderByFechaEnvioDesc(Long conversacionId);

}
