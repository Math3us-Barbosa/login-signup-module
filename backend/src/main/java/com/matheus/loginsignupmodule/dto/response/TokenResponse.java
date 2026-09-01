package com.matheus.loginsignupmodule.dto.response;

public record TokenResponse(
		String token,
		String tipo,
		long expiracaoEmSegundos
) {
}
