package com.nebrimatch.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nebrimatch.backend.dto.LoginRequestDTO;
import com.nebrimatch.backend.dto.UsuarioDTO;
import com.nebrimatch.backend.exception.ConflictException;
import com.nebrimatch.backend.model.RolUsuario;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.RolUsuarioRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// Es equivalente a poner @Autowired en el constructor, pero sin escribirlo manualmente; 
// Lombok lo genera automáticamente y hace que el código sea más limpio genera automáticamente un constructor con todos los campos 
// final de la clase.
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        return toDTO(usuario);
    }

    // Obtener usuario por email
    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return toDTO(usuario);
    }

    // Crear usuario
    public UsuarioDTO crear(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Ya existe un usuario con ese email");
        }
        if (usuarioRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new ConflictException("Ya existe un usuario con ese nombre de usuario");
        }

        Usuario usuario = toEntity(dto);
        Usuario guardado = usuarioRepository.save(usuario);

        if (dto.getRol() != null) {
            RolUsuario rol = new RolUsuario();
            rol.setRol(dto.getRol());
            rol.setUsuario(guardado);
            rolUsuarioRepository.save(rol);
        }

        return toDTO(guardado);
    }

    // Actualizar usuario
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        usuario.setDescripcion(dto.getDescripcion());
        usuario.setLenguajesAEnsenar(dto.getLenguajesAEnsenar());
        usuario.setLenguajesAAprender(dto.getLenguajesAAprender());

        return toDTO(usuarioRepository.save(usuario));
    }

    // Login: verifica email y contraseña
    public UsuarioDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return toDTO(usuario);
    }

    // Eliminar usuario
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Buscar usuarios por lenguaje que enseñan
    public List<UsuarioDTO> buscarPorLenguajeEnsenado(String lenguaje) {
        return usuarioRepository.findByLenguajesAEnsenarContaining(lenguaje)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuarios por lenguaje que aprenden
    public List<UsuarioDTO> buscarPorLenguajeAprendido(String lenguaje) {
        return usuarioRepository.findByLenguajesAAprenderContaining(lenguaje)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Convertir entidad -> DTO
    private UsuarioDTO toDTO(Usuario usuario) {
        RolUsuario rolUsuario = usuario.getRolUsuario();
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                null, // password nunca se devuelve en respuestas
                usuario.getDescripcion(),
                usuario.getLenguajesAEnsenar(),
                usuario.getLenguajesAAprender(),
                usuario.getNumeroMatches(),
                usuario.getFechaCreacion(),
                rolUsuario != null ? rolUsuario.getRol() : null
        );
    }

    // Convertir DTO -> entidad
    private Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setDescripcion(dto.getDescripcion());
        usuario.setLenguajesAEnsenar(dto.getLenguajesAEnsenar());
        usuario.setLenguajesAAprender(dto.getLenguajesAAprender());
        usuario.setNumeroMatches(0);
        return usuario;
    }
}
