package com.daos.security;

public class JWTAuthResponseDTO {

	private String token;

	private String tipo = "Bearer";

	public JWTAuthResponseDTO(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getTipo() {
		return tipo;
	}
}
