package com.matheus.loginsignupmodule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import com.matheus.loginsignupmodule.config.JwtProperties;
import com.matheus.loginsignupmodule.dto.request.LoginRequest;
import com.matheus.loginsignupmodule.dto.response.TokenResponse;
import com.matheus.loginsignupmodule.entity.Role;
import com.matheus.loginsignupmodule.entity.Usuario;
import com.matheus.loginsignupmodule.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private JwtService jwtService;

	@Mock
	private JwtProperties jwtProperties;

	private AuthService authService;

	@BeforeEach
	void setUp() {
		authService = new AuthService(authenticationManager, usuarioRepository, jwtService, jwtProperties);
	}

	@Test
	void deveAutenticarEGerarTokenComIdERoleDoUsuario() {
		UUID usuarioId = UUID.randomUUID();
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		usuario.setEmail("maria@exemplo.com");
		usuario.setRole(Role.PRESTADORA);

		LoginRequest request = new LoginRequest("Maria@Exemplo.com", "senhaSegura123");

		when(usuarioRepository.findByEmail("maria@exemplo.com")).thenReturn(Optional.of(usuario));
		when(jwtService.gerarToken(usuarioId, Role.PRESTADORA)).thenReturn("token-jwt");
		when(jwtProperties.expiration()).thenReturn(Duration.ofHours(1));

		TokenResponse response = authService.login(request);

		assertThat(response.token()).isEqualTo("token-jwt");
		assertThat(response.tipo()).isEqualTo("Bearer");
		assertThat(response.expiracaoEmSegundos()).isEqualTo(3600L);
	}

	@Test
	void devePropagarBadCredentialsQuandoAutenticacaoFalha() {
		LoginRequest request = new LoginRequest("maria@exemplo.com", "senhaErrada");

		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

		assertThatThrownBy(() -> authService.login(request))
				.isInstanceOf(BadCredentialsException.class);
	}

}
