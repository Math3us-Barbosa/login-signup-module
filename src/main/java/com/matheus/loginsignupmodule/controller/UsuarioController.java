package com.matheus.loginsignupmodule.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheus.loginsignupmodule.dto.request.CadastroRequest;
import com.matheus.loginsignupmodule.dto.response.UsuarioResponse;
import com.matheus.loginsignupmodule.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	@PostMapping("/api/auth/cadastro")
	public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroRequest request) {
		UsuarioResponse response = usuarioService.cadastrar(request);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/usuarios/{id}")
				.buildAndExpand(response.id())
				.toUri();

		return ResponseEntity.created(location).body(response);
	}

	@GetMapping("/api/usuarios/me")
	public ResponseEntity<UsuarioResponse> buscarUsuarioAutenticado(Authentication authentication) {
		UUID usuarioId = UUID.fromString(authentication.getName());
		return ResponseEntity.ok(usuarioService.buscarPorId(usuarioId));
	}

}
