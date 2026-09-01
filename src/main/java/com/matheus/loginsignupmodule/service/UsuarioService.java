package com.matheus.loginsignupmodule.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.loginsignupmodule.dto.request.CadastroRequest;
import com.matheus.loginsignupmodule.dto.response.UsuarioResponse;
import com.matheus.loginsignupmodule.entity.Role;
import com.matheus.loginsignupmodule.entity.Usuario;
import com.matheus.loginsignupmodule.exception.EmailJaCadastradoException;
import com.matheus.loginsignupmodule.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UsuarioResponse cadastrar(CadastroRequest request) {
		String email = request.email().toLowerCase();

		if (usuarioRepository.existsByEmail(email)) {
			throw new EmailJaCadastradoException();
		}

		Usuario usuario = new Usuario();
		usuario.setNome(request.nome());
		usuario.setEmail(email);
		usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
		usuario.setTelefone(request.telefone());
		usuario.setRole(Role.PRESTADORA);
		usuario.setAtivo(true);

		Usuario salvo = usuarioRepository.save(usuario);

		return UsuarioResponse.fromEntity(salvo);
	}

	@Transactional(readOnly = true)
	public UsuarioResponse buscarPorId(UUID id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));

		return UsuarioResponse.fromEntity(usuario);
	}

}
