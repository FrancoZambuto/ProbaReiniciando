package com.daos.security;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

	@NotBlank(message = "El usuario es obligatorio")
	private String username;

	@NotBlank(message = "La contraseña es obligatoria")
	private String pass;

	public LoginDTO() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}
