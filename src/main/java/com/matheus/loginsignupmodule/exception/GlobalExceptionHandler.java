package com.matheus.loginsignupmodule.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(EmailJaCadastradoException.class)
	public ProblemDetail handleEmailJaCadastrado(EmailJaCadastradoException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(AuthenticationException.class)
	public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
				"Erro de validação nos campos enviados");

		Map<String, String> erros = new LinkedHashMap<>();
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			erros.putIfAbsent(fieldError.getField(),
					Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Valor inválido"));
		}
		problemDetail.setProperty("errors", erros);

		return problemDetail;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGenericException(Exception ex) {
		log.error("Erro não tratado", ex);
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"Erro interno. Tente novamente mais tarde.");
	}

}
