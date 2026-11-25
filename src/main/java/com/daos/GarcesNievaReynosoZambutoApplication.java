package com.daos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Integrantes:
 * Franco Zambuto - fzambuto@frsf.utn.edu.ar
 */

@OpenAPIDefinition(info = @Info(
		contact = @Contact(
				name = "Garces, Nieva, Reynoso, Zambuto"),
		description = "API REST de la inmobiliaria HogarYA - TP Integrador DAOS 2026",
		title = "HogarYA API",
		version = "1.0"
		),
		security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@SpringBootApplication
public class GarcesNievaReynosoZambutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarcesNievaReynosoZambutoApplication.class, args);
	}

}
