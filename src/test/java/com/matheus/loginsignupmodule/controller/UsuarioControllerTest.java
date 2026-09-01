package com.matheus.loginsignupmodule.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.matheus.loginsignupmodule.dto.response.UsuarioResponse;
import com.matheus.loginsignupmodule.entity.Role;
import com.matheus.loginsignupmodule.exception.EmailJaCadastradoException;
import com.matheus.loginsignupmodule.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UsuarioService usuarioService;

	@Test
	void deveRetornar201ComLocationQuandoCadastroValido() throws Exception {
		UUID id = UUID.randomUUID();
		UsuarioResponse response = new UsuarioResponse(id, "Maria Silva", "maria@exemplo.com", "11987654321",
				Role.PRESTADORA, true, LocalDateTime.now());

		when(usuarioService.cadastrar(any())).thenReturn(response);

		mockMvc.perform(post("/api/auth/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "Maria Silva",
								  "email": "maria@exemplo.com",
								  "telefone": "11987654321",
								  "senha": "senhaSegura123"
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("/api/usuarios/" + id)))
				.andExpect(jsonPath("$.email").value("maria@exemplo.com"))
				.andExpect(jsonPath("$.role").value("PRESTADORA"));
	}

	@Test
	void deveRetornar400QuandoCamposInvalidos() throws Exception {
		mockMvc.perform(post("/api/auth/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "",
								  "email": "email-invalido",
								  "senha": "123"
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors.nome").exists())
				.andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.senha").exists());
	}

	@Test
	void deveRetornar409QuandoEmailJaCadastrado() throws Exception {
		when(usuarioService.cadastrar(any())).thenThrow(new EmailJaCadastradoException());

		mockMvc.perform(post("/api/auth/cadastro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "nome": "Maria Silva",
								  "email": "maria@exemplo.com",
								  "telefone": "11987654321",
								  "senha": "senhaSegura123"
								}
								"""))
				.andExpect(status().isConflict());
	}

	@Test
	void deveRetornar200ComDadosDoUsuarioAutenticadoQuandoBuscarMe() throws Exception {
		UUID id = UUID.randomUUID();
		UsuarioResponse response = new UsuarioResponse(id, "Maria Silva", "maria@exemplo.com", "11987654321",
				Role.PRESTADORA, true, LocalDateTime.now());

		when(usuarioService.buscarPorId(id)).thenReturn(response);

		mockMvc.perform(get("/api/usuarios/me")
						.principal(new UsernamePasswordAuthenticationToken(id.toString(), null)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("maria@exemplo.com"));
	}

}
