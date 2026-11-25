package com.daos.presentacion.Incidentes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.HistorialEstadoIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.PrioridadIncidente;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.contratos.ContratoRestController;
import com.daos.presentacion.errores.ErrorInfo;
import com.daos.servicios.IncidenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/incidentes")
@Tag(name = "Incidentes", description = "S03 - Incidentes de mantenimiento: registrar, consultar y gestionar su seguimiento")
public class IncidenteRestController {

	@Autowired
	private IncidenteService service;

	@Operation(summary = "Registra un incidente de mantenimiento sobre un contrato vigente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Incidente registrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = IncidenteResponseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Datos faltantes o inválidos", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "404", description = "Contrato inexistente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "409", description = "Contrato eliminado, no activo o no vigente en la fecha de alta", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<IncidenteResponseDTO> crear(@Valid @RequestBody IncidenteForm form, BindingResult result)
			throws Excepcion {
		if (result.hasErrors())
			throw new Excepcion(this.formatearError(result), 400);

		Incidente pojo = service.crear(form.toPojo(), form.getContratoId());
		return new ResponseEntity<IncidenteResponseDTO>(buildResponse(pojo), HttpStatus.CREATED);
	}

	@Operation(summary = "Obtiene un incidente a partir de su id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Incidente encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = IncidenteResponseDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Incidente no encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<IncidenteResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Incidente pojo = service.getById(id);
		return new ResponseEntity<IncidenteResponseDTO>(buildResponse(pojo), HttpStatus.OK);
	}

	@Operation(summary = "Lista los incidentes, permitiendo filtrar por contrato, estado, categoría y prioridad")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Incidentes encontrados"),
			@ApiResponse(responseCode = "400", description = "Filtro inválido", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<IncidenteResponseDTO> filtrar(@RequestParam(name = "contratoId", required = false) Long contratoId,
			@RequestParam(name = "estado", required = false) EstadoIncidente estado,
			@RequestParam(name = "categoria", required = false) CategoriaIncidente categoria,
			@RequestParam(name = "prioridad", required = false) PrioridadIncidente prioridad) throws Excepcion {

		List<Incidente> incidentes = service.filtrar(contratoId, estado, categoria, prioridad);
		List<IncidenteResponseDTO> dtos = new ArrayList<IncidenteResponseDTO>();
		for (Incidente pojo : incidentes) {
			dtos.add(buildResponse(pojo));
		}
		return dtos;
	}

	@Operation(summary = "Cambia el estado de un incidente según las transiciones permitidas (la resolución se realiza mediante S04)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Estado actualizado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = IncidenteResponseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Estado faltante o inválido", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "404", description = "Incidente no encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "409", description = "Transición de estado no permitida", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@PatchMapping(value = "/{id}/estado", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<IncidenteResponseDTO> cambiarEstado(@PathVariable Long id,
			@Valid @RequestBody IncidenteEstadoForm form, BindingResult result) throws Excepcion {
		if (result.hasErrors())
			throw new Excepcion(this.formatearError(result), 400);

		Incidente pojo = service.cambiarEstado(id, form.getEstado());
		return new ResponseEntity<IncidenteResponseDTO>(buildResponse(pojo), HttpStatus.OK);
	}

	@Operation(summary = "Consulta el historial de estados de un incidente, ordenado cronológicamente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Historial encontrado"),
			@ApiResponse(responseCode = "404", description = "Incidente no encontrado", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(value = "/{id}/historial", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<HistorialEstadoIncidenteResponseDTO> getHistorial(@PathVariable Long id) throws Excepcion {
		List<HistorialEstadoIncidente> historial = service.getHistorial(id);
		List<HistorialEstadoIncidenteResponseDTO> dtos = new ArrayList<HistorialEstadoIncidenteResponseDTO>();
		for (HistorialEstadoIncidente pojo : historial) {
			HistorialEstadoIncidenteResponseDTO dto = new HistorialEstadoIncidenteResponseDTO(pojo);
			dto.add(WebMvcLinkBuilder.linkTo(IncidenteRestController.class).slash(id).slash("historial")
					.withSelfRel());
			dto.add(WebMvcLinkBuilder.linkTo(IncidenteRestController.class).slash(id).withRel("incidente"));
			dtos.add(dto);
		}
		return dtos;
	}

	private IncidenteResponseDTO buildResponse(Incidente pojo) throws Excepcion {
		IncidenteResponseDTO dto = new IncidenteResponseDTO(pojo);

		Link selfLink = WebMvcLinkBuilder.linkTo(IncidenteRestController.class)
				.slash(pojo.getId())
				.withSelfRel();

		Link contratoLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(ContratoRestController.class)
						.getById(pojo.getContrato().getId()))
				.withRel("contrato");

		// S04 todavía no está en este proyecto: se arma el link con la ruta acordada
		String base = WebMvcLinkBuilder.linkTo(IncidenteRestController.class).toUri()
				.resolve("/informes-resolucion").toString();
		Link informesLink = Link.of(base + "?incidenteId=" + pojo.getId(), "informes");

		dto.add(selfLink);
		dto.add(contratoLink);
		dto.add(informesLink);

		return dto;
	}

	private String formatearError(BindingResult result) {
		String err = "";
		for (FieldError error : result.getFieldErrors()) {
			err += error.getField() + ": " + error.getDefaultMessage() + "\n";
		}
		return err;
	}
}
