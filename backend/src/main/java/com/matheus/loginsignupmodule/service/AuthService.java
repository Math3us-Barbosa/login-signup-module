package com.matheus.loginsignupmodule.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.loginsignupmodule.config.JwtProperties;
import com.matheus.loginsignupmodule.dto.request.LoginRequest;
import com.matheus.loginsignupmodule.dto.response.TokenResponse;
import com.matheus.loginsignupmodule.entity.Usuario;
import com.matheus.loginsignupmodule.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private static final String TIPO_TOKEN = "Bearer";

	private final AuthenticationManager authenticationManager;
	private final UsuarioRepository usuarioRepository;
	private final JwtService jwtService;
	private final JwtProperties jwtProperties;

	@Transactional(readOnly = true)
	public TokenResponse login(LoginRequest request) {
		String email = request.email().toLowerCase();

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.senha()));

		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado"));

		String token = jwtService.gerarToken(usuario.getId(), usuario.getRole());

		return new TokenResponse(token, TIPO_TOKEN, jwtProperties.expiration().toSeconds());
	}

}
