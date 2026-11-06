package com.daos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

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
		)
)
@SpringBootApplication
public class GarcesNievaReynosoZambutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarcesNievaReynosoZambutoApplication.class, args);
	}

}
