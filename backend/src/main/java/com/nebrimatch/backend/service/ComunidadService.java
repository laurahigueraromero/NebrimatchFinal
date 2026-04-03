package com.nebrimatch.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nebrimatch.backend.dto.ComunidadDTO;
import com.nebrimatch.backend.model.Comunidad;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.ComunidadRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ComunidadService {

    private final ComunidadRepository comunidadRepository;
    private final UsuarioRepository usuarioRepository;

    // crear comunidad y que no exista!! ==>
    public ComunidadDTO crearComunidad(String nombre, String descripcion, String imagenComunidad, Long creadorId) {

        if (creadorId == null) {
            throw new RuntimeException("El creador de la comunidad no puede ser nulo");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new RuntimeException("El nombre de la comunidad no puede estar vacío");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new RuntimeException("La descripción de la comunidad no puede estar vacía");
        }

        Usuario creador = usuarioRepository.findById(creadorId)
                .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));

        if (comunidadRepository.existsByNombreComunidad(nombre)) {
            throw new RuntimeException("Ya existe una comunidad con ese nombre");
        }

        Comunidad comunidad = new Comunidad();
        comunidad.setNombreComunidad(nombre);
        comunidad.setDescripcionComunidad(descripcion);
        comunidad.setImagenComunidad(imagenComunidad);
        comunidad.setCreadorComunidad(creador);

        Comunidad guardada = comunidadRepository.save(comunidad);

        return toComunidadDTO(guardada);
    }

    public ComunidadDTO obtenerComunidadPorId(Long id) {
        Comunidad comunidad = comunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        return toComunidadDTO(comunidad);
    }

    // por creador ==>

     public List <ComunidadDTO> obtenerComunidadPorCreador(Long creadorId) {
        
            Usuario creador = usuarioRepository.findById(creadorId)
                    .orElseThrow(() -> new RuntimeException("Usuario creador no encontrado"));              

            return comunidadRepository.findByCreadorComunidad(creador)
                    .stream()
                    .map(this::toComunidadDTO)
                    .collect(Collectors.toList());
     }
   
    public List<ComunidadDTO> obtenerTodasLasComunidades() {
        return comunidadRepository.findAll()
                .stream()
                .map(this::toComunidadDTO)
                .collect(Collectors.toList());
    }

    public List<ComunidadDTO> buscarComunidades(String texto){

      return comunidadRepository.findByNombreComunidadContainingIgnoreCase(texto)
                  .stream()
                  .map(this::toComunidadDTO)
                  .collect(Collectors.toList());

    }

    // actualizar comunidad ==>
    public ComunidadDTO actualizarComunidad(Long idComunidad, String nombre, String descripcion, String imagenComunidad, Long solicitanteId) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        if (!comunidad.getCreadorComunidad().getId().equals(solicitanteId)) {
            throw new RuntimeException("Solo el creador puede modificar la comunidad");
        }

        comunidad.setNombreComunidad(nombre);
        comunidad.setDescripcionComunidad(descripcion);
        comunidad.setImagenComunidad(imagenComunidad);

        return toComunidadDTO(comunidadRepository.save(comunidad));
    }

    // eliminar comunidad ==>
    public void eliminarComunidad(Long idComunidad, Long solicitanteId) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        if (!comunidad.getCreadorComunidad().getId().equals(solicitanteId)) {
            throw new RuntimeException("Solo el creador puede eliminar la comunidad");
        }

        comunidadRepository.deleteById(idComunidad);
    }




    // mapper privado ==>
      // Base de datos → Entidad (Comunidad) → DTO (ComunidadDTO) → Frontend
      // El mapper hace la transformación del medio. El frontend nunca ve la entidad directamente, solo el DTO con los campos que tú decides exponer.
    private ComunidadDTO toComunidadDTO(Comunidad comunidad) {
        return new ComunidadDTO(
                comunidad.getId(),
                comunidad.getNombreComunidad(),
                comunidad.getDescripcionComunidad(),
                comunidad.getImagenComunidad(),
                comunidad.getFechaCreacion(),
                comunidad.getCreadorComunidad().getId(),
                comunidad.getCreadorComunidad().getNombreUsuario()
        );
    }
}
