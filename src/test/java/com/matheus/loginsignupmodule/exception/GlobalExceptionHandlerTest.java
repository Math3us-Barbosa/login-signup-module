package com.matheus.loginsignupmodule.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void deveRetornar500ComMensagemGenericaSemExporDetalheDaExcecao() {
		Exception excecaoComDadoSensivel = new RuntimeException("falha ao processar maria@exemplo.com");

		ProblemDetail problemDetail = handler.handleGenericException(excecaoComDadoSensivel);

		assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
		assertThat(problemDetail.getDetail())
				.isEqualTo("Erro interno. Tente novamente mais tarde.")
				.doesNotContain("maria@exemplo.com");
	}

}
