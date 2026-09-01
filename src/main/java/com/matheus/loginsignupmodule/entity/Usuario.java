package com.matheus.loginsignupmodule.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue
	@UuidGenerator(style = UuidGenerator.Style.RANDOM)
	@Column(updatable = false, nullable = false)
	private UUID id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "senha_hash", nullable = false)
	private String senhaHash;

	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private boolean ativo = true;

	@CreationTimestamp
	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@UpdateTimestamp
	@Column(name = "atualizado_em", nullable = false)
	private LocalDateTime atualizadoEm;

}
