package com.matheus.loginsignupmodule.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.matheus.loginsignupmodule.config.JwtProperties;
import com.matheus.loginsignupmodule.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private static final String CLAIM_ROLE = "role";

	private final JwtProperties jwtProperties;

	public String gerarToken(UUID usuarioId, Role role) {
		Instant agora = Instant.now();
		return Jwts.builder()
				.subject(usuarioId.toString())
				.claim(CLAIM_ROLE, role.name())
				.issuedAt(Date.from(agora))
				.expiration(Date.from(agora.plus(jwtProperties.expiration())))
				.signWith(getSigningKey())
				.compact();
	}

	public UUID extrairUsuarioId(String token) {
		return UUID.fromString(parseClaims(token).getSubject());
	}

	public Role extrairRole(String token) {
		return Role.valueOf(parseClaims(token).get(CLAIM_ROLE, String.class));
	}

	public boolean isTokenValido(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
	}

}
