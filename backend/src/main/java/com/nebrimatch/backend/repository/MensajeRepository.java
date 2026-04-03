package com.nebrimatch.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nebrimatch.backend.model.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // buscar mensajes por conversacion
    List<Mensaje> findByConversacionId(Long conversacionId);

    // mensajes ordenados por fecha (para mostrar el chat en orden cronológico)
    List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Long conversacionId);

    // los últimos N mensajes de una conversación (para previsualización en la lista
    // de chats) en vez de que salgan los primeros que serían los más antiguos
    List<Mensaje> findTop1ByConversacionIdOrderByFechaEnvioDesc(Long conversacionId);

}
