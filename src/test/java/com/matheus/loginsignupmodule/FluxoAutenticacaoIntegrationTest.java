package com.matheus.loginsignupmodule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.matheus.loginsignupmodule.dto.response.TokenResponse;

import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
class FluxoAutenticacaoIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void deveCadastrarLogarEAcessarRotaProtegidaComToken() throws Exception {
		String email = "usuaria-" + UUID.randomUUID() + "@exemplo.com";
		String senha = "senhaSegura123";

		mockMvc.perform(post("/api/auth/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "Maria Silva",
								  "email": "%s",
								  "telefone": "11987654321",
								  "senha": "%s"
								}
								""".formatted(email, senha)))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/api/usuarios/me"))
				.andExpect(status().isUnauthorized());

		MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "email": "%s",
								  "senha": "%s"
								}
								""".formatted(email, senha)))
				.andExpect(status().isOk())
				.andReturn();

		TokenResponse tokenResponse = objectMapper.readValue(
				loginResult.getResponse().getContentAsString(), TokenResponse.class);

		mockMvc.perform(get("/api/usuarios/me")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.token()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value(email));
	}

}
