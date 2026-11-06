package com.daos.presentacion.contratos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daos.entidades.Contrato;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.errores.ErrorInfo;
import com.daos.servicios.ContratoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contratos")
@Tag(name = "Contratos", description = "Consulta de contratos")
public class ContratoRestController {

	@Autowired
	private ContratoService service;

	@Operation(summary = "Obtiene un contrato a partir de su id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Contrato encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContratoResponseDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Contrato no encontrado o eliminado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ContratoResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Contrato pojo = service.getById(id);
		return new ResponseEntity<ContratoResponseDTO>(buildResponse(pojo), HttpStatus.OK);
	}

	private ContratoResponseDTO buildResponse(Contrato pojo) {
		ContratoResponseDTO dto = new ContratoResponseDTO(pojo);

		Link selfLink = WebMvcLinkBuilder.linkTo(ContratoRestController.class)
				.slash(pojo.getId())
				.withSelfRel();
		dto.add(selfLink);
		return dto;
	}
}
