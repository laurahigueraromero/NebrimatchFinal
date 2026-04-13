package com.nebrimatch.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nebrimatch.backend.dto.MatchUsuarioDTO;
import com.nebrimatch.backend.exception.ConflictException;
import com.nebrimatch.backend.exception.NotFoundException;
import com.nebrimatch.backend.model.MatchUsuario;
import com.nebrimatch.backend.model.Usuario;
import com.nebrimatch.backend.repository.MatchUsuarioRepository;
import com.nebrimatch.backend.repository.UsuarioRepository;
import com.nebrimatch.backend.service.mapper.MatchMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchUsuarioService {

	private final MatchUsuarioRepository matchUsuarioRepository;
	private final UsuarioRepository usuarioRepository;
	private final MatchMapper matchMapper;

	@Transactional
	public MatchUsuarioDTO crearMatch(Long usuario1Id, Long usuario2Id) {
		if (usuario1Id.equals(usuario2Id)) {
			throw new ConflictException("No se puede hacer match consigo mismo");
		}
				// buscar ususarios por id para comprobar que existen ==>
		Usuario u1 = usuarioRepository.findById(usuario1Id)
				.orElseThrow(() -> new NotFoundException("Usuario 1 no encontrado"));

		Usuario u2 = usuarioRepository.findById(usuario2Id)
				.orElseThrow(() -> new NotFoundException("Usuario 2 no encontrado"));
				// boleano si existen los usuarios ya con match
		boolean existe = matchUsuarioRepository
				.existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(u1, u2, u1, u2);
	// si se da la condicion de que existe el match se lanza excepcion de conflicto
		if (existe) {
			throw new ConflictException("Ya existe un match entre estos usuarios");
		}
// si no, seguimos con proceso y se guarda el match en la base de datos
		MatchUsuario match = new MatchUsuario();
		match.setUsuario1(u1);
		match.setUsuario2(u2);
		// guardamos el match
		MatchUsuario guardado = matchUsuarioRepository.save(match);

		log.info("Match creado: {} entre {} y {}", guardado.getId(), usuario1Id, usuario2Id);
		// mappeamos el match guardado a DTO para devolverlo al controlador (DTO siempre es lo que se devuelve al controlador, no la entidad)
		return matchMapper.toMatchUsuarioDTO(guardado);
	}

	public List<MatchUsuarioDTO> obtenerMatchesPorUsuario(Long usuarioId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

		return matchUsuarioRepository.findByUsuario1OrUsuario2(usuario, usuario)
			.stream()
			.map(matchMapper::toMatchUsuarioDTO)
			.collect(Collectors.toList());
	}

	public MatchUsuarioDTO obtenerMatchPorId(Long matchId) {
		MatchUsuario match = matchUsuarioRepository.findById(matchId)
			.orElseThrow(() -> new NotFoundException("Match no encontrado"));

		return matchMapper.toMatchUsuarioDTO(match);
	}

	@Transactional
	public void eliminarMatch(Long matchId) {
		MatchUsuario match = matchUsuarioRepository.findById(matchId)
				.orElseThrow(() -> new NotFoundException("Match no encontrado"));

		matchUsuarioRepository.delete(match);
		log.info("Match eliminado: {}", matchId);
	}

    
}
