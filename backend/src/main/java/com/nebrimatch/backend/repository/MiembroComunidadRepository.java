package com.nebrimatch.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nebrimatch.backend.model.Comunidad;
import com.nebrimatch.backend.model.MiembroComunidad;
import com.nebrimatch.backend.model.Usuario;

public interface MiembroComunidadRepository extends JpaRepository<MiembroComunidad, Long> {

    boolean existsByUsuarioAndComunidad(Usuario usuario, Comunidad comunidad);

    Optional<MiembroComunidad> findByUsuarioAndComunidad(Usuario usuario, Comunidad comunidad);

    long countByComunidad(Comunidad comunidad);
}
