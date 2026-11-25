package com.daos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daos.excepciones.Excepcion;
import com.daos.presentacion.errores.ErrorInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@Tag(name = "Login", description = "Autenticación: obtiene el token JWT a enviar en el resto de las peticiones")
public class LoginRestController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Operation(summary = "Autentica un usuario y devuelve un token JWT")
	@SecurityRequirements
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario autenticado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = JWTAuthResponseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Usuario o contraseña faltantes", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> authenticatedUser(@Valid @RequestBody LoginDTO credenciales, BindingResult result)
			throws Excepcion {
		if (result.hasErrors())
			throw new Excepcion(this.formatearError(result), 400);

		Authentication authentication;
		try {
			authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(credenciales.getUsername(), credenciales.getPass()));
		} catch (AuthenticationException e) {
			throw new Excepcion("Usuario o contraseña incorrectos", 401);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtTokenProvider.generarToken(authentication);
		return ResponseEntity.ok(new JWTAuthResponseDTO(token));
	}

	private String formatearError(BindingResult result) {
		String err = "";
		for (FieldError error : result.getFieldErrors()) {
			err += error.getField() + ": " + error.getDefaultMessage() + "\n";
		}
		return err;
	}
}
