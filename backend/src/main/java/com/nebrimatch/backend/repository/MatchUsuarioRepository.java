package com.nebrimatch.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nebrimatch.backend.model.MatchUsuario;
import com.nebrimatch.backend.model.Usuario;

@Repository
public interface MatchUsuarioRepository extends JpaRepository<MatchUsuario, Long> {

    // obtener todos los matches de un usuario (puede ser usuario1 o usuario2)
    List<MatchUsuario> findByUsuario1OrUsuario2(Usuario usuario1, Usuario usuario2);

    // comprobar si ya existe un match entre dos usuarios
    boolean existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(
            Usuario usuario1, Usuario usuario2,
            Usuario usuario2b, Usuario usuario1b);

}
