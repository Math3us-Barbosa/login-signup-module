package com.matheus.loginsignupmodule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CadastroRequest(

		@NotBlank(message = "Nome é obrigatório")
		@Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
		String nome,

		@NotBlank(message = "Email é obrigatório")
		@Email(message = "Email inválido")
		@Size(max = 180, message = "Email deve ter no máximo 180 caracteres")
		String email,

		@Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve conter 10 ou 11 dígitos")
		String telefone,

		@NotBlank(message = "Senha é obrigatória")
		@Size(min = 8, max = 72, message = "Senha deve ter entre 8 e 72 caracteres")
		String senha

) {
}
