package com.matheus.loginsignupmodule.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.matheus.loginsignupmodule.dto.response.TokenResponse;
import com.matheus.loginsignupmodule.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;

	@Test
	void deveRetornar200ComTokenQuandoCredenciaisValidas() throws Exception {
		when(authService.login(any())).thenReturn(new TokenResponse("token-jwt", "Bearer", 3600L));

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "email": "maria@exemplo.com",
								  "senha": "senhaSegura123"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("token-jwt"))
				.andExpect(jsonPath("$.tipo").value("Bearer"))
				.andExpect(jsonPath("$.expiracaoEmSegundos").value(3600));
	}

	@Test
	void deveRetornar401ComMensagemGenericaQuandoCredenciaisInvalidas() throws Exception {
		when(authService.login(any())).thenThrow(new BadCredentialsException("Bad credentials"));

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "email": "maria@exemplo.com",
								  "senha": "senhaErrada"
								}
								"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.detail").value("Email ou senha inválidos"));
	}

	@Test
	void deveRetornar400QuandoCamposInvalidos() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "email": "email-invalido",
								  "senha": ""
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.senha").exists());
	}

	@Test
	void deveRetornar200NoHealthCheck() throws Exception {
		mockMvc.perform(get("/api/auth/health"))
				.andExpect(status().isOk());
	}

}
