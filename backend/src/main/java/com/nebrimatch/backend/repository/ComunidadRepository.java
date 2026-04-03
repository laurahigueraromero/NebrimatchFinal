package com.nebrimatch.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nebrimatch.backend.model.Comunidad;
import com.nebrimatch.backend.model.Usuario;

@Repository
public interface ComunidadRepository extends JpaRepository<Comunidad, Long> {

    // buscamos por nombre de comunidad => ponemos optional para que no salga una
    // null exception si no se encuentra la comunidad
    Optional<Comunidad> findByNombreComunidad(String nombreComunidad);

    // comprobar si ya existe una comunidad con ese nombre (para validaciones)
    boolean existsByNombreComunidad(String nombreComunidad);

    // buscar todas las comunidades creadas por un usuario (puede tener varias por eso ponemos List)
    List<Comunidad> findByCreadorComunidad(Usuario creadorComunidad);


    // para el buscador!!!
    List<Comunidad> findByNombreComunidadContainingIgnoreCase(String texto);

}
