package com.daos.presentacion.facturas;

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

import com.daos.entidades.EstadoFactura;
import com.daos.entidades.Factura;
import com.daos.entidades.TipoFactura;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.contratos.ContratoRestController;
import com.daos.presentacion.errores.ErrorInfo;
import com.daos.servicios.FacturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/facturas")
@Tag(name = "Facturas", description = "S01 - Facturas de alquiler: emitir, consultar y anular facturas")
public class FacturaRestController {

	@Autowired
	private FacturaService service;

	@Operation(summary = "Emite una factura. Indicando solo contrato y período se genera la facturación mensual; indicando el importe se genera una factura adicional")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Factura emitida", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Datos faltantes o inválidos", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "404", description = "Contrato inexistente", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "409", description = "Contrato no activo, período fuera de vigencia, facturación mensual duplicada o emisión posterior al vencimiento", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FacturaResponseDTO> emitir(@Valid @RequestBody FacturaForm form, BindingResult result)
			throws Excepcion {
		if (result.hasErrors())
			throw new Excepcion(this.formatearError(result), 400);

		Factura pojo = service.emitir(form.toPojo(), form.getContratoId());
		return new ResponseEntity<FacturaResponseDTO>(buildResponse(pojo), HttpStatus.CREATED);
	}

	@Operation(summary = "Obtiene una factura a partir de su id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Factura encontrada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponseDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Factura no encontrada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Long id) throws Excepcion {
		Factura pojo = service.getById(id);
		return new ResponseEntity<FacturaResponseDTO>(buildResponse(pojo), HttpStatus.OK);
	}

	@Operation(summary = "Lista las facturas, permitiendo filtrar por contrato, período (AAAA-MM), estado y tipo")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Facturas encontradas"),
			@ApiResponse(responseCode = "400", description = "Filtro inválido", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<FacturaResponseDTO> filtrar(@RequestParam(name = "contratoId", required = false) Long contratoId,
			@RequestParam(name = "periodo", required = false) String periodo,
			@RequestParam(name = "estado", required = false) EstadoFactura estado,
			@RequestParam(name = "tipo", required = false) TipoFactura tipo) throws Excepcion {

		List<Factura> facturas = service.filtrar(contratoId, periodo, estado, tipo);
		List<FacturaResponseDTO> dtos = new ArrayList<FacturaResponseDTO>();
		for (Factura pojo : facturas) {
			dtos.add(buildResponse(pojo));
		}
		return dtos;
	}

	@Operation(summary = "Anula una factura pendiente o vencida (solo se admite el estado ANULADA)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Factura anulada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = FacturaResponseDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Estado faltante o inválido", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "404", description = "Factura no encontrada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }),
			@ApiResponse(responseCode = "409", description = "Transición de estado no permitida", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class)) }) })
	@PatchMapping(value = "/{id}/estado", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FacturaResponseDTO> cambiarEstado(@PathVariable Long id,
			@Valid @RequestBody EstadoFacturaForm form, BindingResult result) throws Excepcion {
		if (result.hasErrors())
			throw new Excepcion(this.formatearError(result), 400);

		Factura pojo = service.cambiarEstado(id, form.getEstado());
		return new ResponseEntity<FacturaResponseDTO>(buildResponse(pojo), HttpStatus.OK);
	}

	private FacturaResponseDTO buildResponse(Factura pojo) throws Excepcion {
		FacturaResponseDTO dto = new FacturaResponseDTO(pojo);

		Link selfLink = WebMvcLinkBuilder.linkTo(FacturaRestController.class)
				.slash(pojo.getId())
				.withSelfRel();

		Link contratoLink = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(ContratoRestController.class)
						.getById(pojo.getContrato().getId()))
				.withRel("contrato");
		dto.add(selfLink);
		dto.add(contratoLink);

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
