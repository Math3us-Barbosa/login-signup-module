package com.matheus.loginsignupmodule.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.matheus.loginsignupmodule.entity.Role;
import com.matheus.loginsignupmodule.entity.Usuario;

public record UsuarioResponse(
		UUID id,
		String nome,
		String email,
		String telefone,
		Role role,
		boolean ativo,
		LocalDateTime criadoEm
) {

	public static UsuarioResponse fromEntity(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getNome(),
				usuario.getEmail(),
				usuario.getTelefone(),
				usuario.getRole(),
				usuario.isAtivo(),
				usuario.getCriadoEm()
		);
	}

}
