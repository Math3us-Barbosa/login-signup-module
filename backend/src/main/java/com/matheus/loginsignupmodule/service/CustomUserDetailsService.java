package com.matheus.loginsignupmodule.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.loginsignupmodule.entity.Usuario;
import com.matheus.loginsignupmodule.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private static final String CREDENCIAIS_INVALIDAS = "Credenciais inválidas";

	private final UsuarioRepository usuarioRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.filter(Usuario::isAtivo)
				.orElseThrow(() -> new UsernameNotFoundException(CREDENCIAIS_INVALIDAS));
		return toUserDetails(usuario);
	}

	@Transactional(readOnly = true)
	public UserDetails loadUserById(UUID id) {
		Usuario usuario = usuarioRepository.findById(id)
				.filter(Usuario::isAtivo)
				.orElseThrow(() -> new UsernameNotFoundException(CREDENCIAIS_INVALIDAS));
		return toUserDetails(usuario);
	}

	private UserDetails toUserDetails(Usuario usuario) {
		return User.withUsername(usuario.getId().toString())
				.password(usuario.getSenhaHash())
				.authorities("ROLE_" + usuario.getRole().name())
				.build();
	}

}
