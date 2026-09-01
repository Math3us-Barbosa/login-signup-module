package com.matheus.loginsignupmodule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.matheus.loginsignupmodule.dto.request.CadastroRequest;
import com.matheus.loginsignupmodule.dto.response.UsuarioResponse;
import com.matheus.loginsignupmodule.entity.Role;
import com.matheus.loginsignupmodule.entity.Usuario;
import com.matheus.loginsignupmodule.exception.EmailJaCadastradoException;
import com.matheus.loginsignupmodule.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private UsuarioService usuarioService;

	@BeforeEach
	void setUp() {
		usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
	}

	@Test
	void deveCadastrarUsuarioComRolePrestadoraEAtivo() {
		CadastroRequest request = new CadastroRequest("Maria Silva", "maria@exemplo.com", "11987654321", "senhaSegura123");

		when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(false);
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		UsuarioResponse response = usuarioService.cadastrar(request);

		assertThat(response.role()).isEqualTo(Role.PRESTADORA);
		assertThat(response.ativo()).isTrue();
	}

	@Test
	void deveNormalizarEmailParaMinusculasAoCadastrar() {
		CadastroRequest request = new CadastroRequest("Maria Silva", "Maria@Exemplo.COM", "11987654321", "senhaSegura123");

		when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(false);
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		UsuarioResponse response = usuarioService.cadastrar(request);

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());

		assertThat(captor.getValue().getEmail()).isEqualTo("maria@exemplo.com");
		assertThat(response.email()).isEqualTo("maria@exemplo.com");
	}

	@Test
	void deveHashearSenhaComBCryptAoCadastrar() {
		String senhaCrua = "senhaSegura123";
		CadastroRequest request = new CadastroRequest("Maria Silva", "maria@exemplo.com", "11987654321", senhaCrua);

		when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(false);
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		usuarioService.cadastrar(request);

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());
		String senhaHash = captor.getValue().getSenhaHash();

		assertThat(senhaHash).isNotEqualTo(senhaCrua);
		assertThat(passwordEncoder.matches(senhaCrua, senhaHash)).isTrue();
	}

	@Test
	void deveLancarEmailJaCadastradoQuandoEmailDuplicado() {
		CadastroRequest request = new CadastroRequest("Maria Silva", "maria@exemplo.com", "11987654321", "senhaSegura123");

		when(usuarioRepository.existsByEmail("maria@exemplo.com")).thenReturn(true);

		assertThatThrownBy(() -> usuarioService.cadastrar(request))
				.isInstanceOf(EmailJaCadastradoException.class);

		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void deveBuscarUsuarioAutenticadoPorId() {
		UUID id = UUID.randomUUID();
		Usuario usuario = new Usuario();
		usuario.setId(id);
		usuario.setNome("Maria Silva");
		usuario.setEmail("maria@exemplo.com");
		usuario.setRole(Role.PRESTADORA);
		usuario.setAtivo(true);

		when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

		UsuarioResponse response = usuarioService.buscarPorId(id);

		assertThat(response.id()).isEqualTo(id);
		assertThat(response.email()).isEqualTo("maria@exemplo.com");
	}

}
