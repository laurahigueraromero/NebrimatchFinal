package com.nebrimatch.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nebrimatch.backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
// long es ID numérico autoincremental

    // Buscar por email o nombreUsuario (login)
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Comprobar si ya existe un email o nombre de usuario (útil para registro)
    boolean existsByEmail(String email);
    boolean existsByNombreUsuario(String nombreUsuario);

    // Buscar usuarios que enseñen un lenguaje concreto
    List<Usuario> findByLenguajesAEnsenarContaining(String lenguaje);

    // Buscar usuarios que quieran aprender un lenguaje concreto
    List<Usuario> findByLenguajesAAprenderContaining(String lenguaje);
}
