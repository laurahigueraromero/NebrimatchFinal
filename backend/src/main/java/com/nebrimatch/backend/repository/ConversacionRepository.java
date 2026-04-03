package com.nebrimatch.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nebrimatch.backend.model.Conversacion;
import com.nebrimatch.backend.model.Usuario;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {

    // buscar conversaciones por usuario (tanto si es emisor como receptor)
    List<Conversacion> findByUsuario1OrUsuario2(Usuario usuario1, Usuario usuario2);

    // buscar conversación entre dos usuarios específicos (para evitar duplicados)
    Optional<Conversacion> findByUsuario1AndUsuario2(Usuario usuario1, Usuario usuario2);

    // ¿existe ya conversacion??
    boolean existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(
            Usuario u1, Usuario u2, Usuario u3, Usuario u4);

}
