package com.daos.presentacion.pagos;

import java.util.List;
import java.util.ArrayList;

import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.daos.entidades.Pago;
import com.daos.servicios.PagoService;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.facturas.FacturaRestController;
import com.daos.presentacion.errores.ErrorInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagos")
@Tag(name = "Pagos", description = "S02 - Pagos: registrar y consultar pagos de facturas")
public class PagoRestController {

    private final PagoService pagoService;

    public PagoRestController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(summary = "Registra el pago de una factura")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Pago registrado correctamente", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = PagoResponseDTO.class))
    }),
    @ApiResponse(responseCode = "400", description = "Datos faltantes o inválidos, fecha o importe incorrecto", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))
    }),
    @ApiResponse(responseCode = "404", description = "Factura inexistente", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))
    }),
    @ApiResponse(responseCode = "409", description = "La factura no permite pagos o ya tiene un pago registrado", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))
    })
})
    @PostMapping
    public ResponseEntity<PagoResponseDTO> registrar(
            @Valid @RequestBody PagoForm form) throws Excepcion {

        Pago pago = new Pago();
        pago.setFechaPago(
          form.getFechaPago() != null
            ? form.getFechaPago()
            : java.time.LocalDate.now()
        );
        pago.setMedioPago(form.getMedioPago());
        pago.setImportePagado(form.getImportePagado());
        pago.setInteres(form.getInteres());

        Pago registrado = pagoService.registrar(pago, form.getFacturaId());

        PagoResponseDTO dto = convertirDTO(registrado);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Obtiene un pago a partir de su id")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pago encontrado", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = PagoResponseDTO.class))
    }),
    @ApiResponse(responseCode = "404", description = "Pago inexistente", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))
    })
})
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagoResponseDTO>> getById(
        @PathVariable Long id) throws Excepcion {

        Pago pago = pagoService.getById(id);

        PagoResponseDTO dto = convertirDTO(pago);
        EntityModel<PagoResponseDTO> recurso = EntityModel.of(dto);

        agregarLinks(pago, recurso);

        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Lista los pagos asociados a una factura")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pagos encontrados"),
    @ApiResponse(responseCode = "400", description = "Identificador de factura inválido", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorInfo.class))
    })
})
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagoResponseDTO>>> filtrar(
        @RequestParam Long facturaId) throws Excepcion {

        List<Pago> pagos = pagoService.filtrar(facturaId);
        List<EntityModel<PagoResponseDTO>> recursos = new ArrayList<>();

        for (Pago pago : pagos) {
            PagoResponseDTO dto = convertirDTO(pago);
            EntityModel<PagoResponseDTO> recurso = EntityModel.of(dto);

            agregarLinks(pago, recurso);

            recursos.add(recurso);
        }

        return ResponseEntity.ok(CollectionModel.of(recursos));
    }

    private PagoResponseDTO convertirDTO(Pago pago) {
        return new PagoResponseDTO(
                pago.getId(),
                pago.getFactura().getId(),
                pago.getFechaPago(),
                pago.getMedioPago(),
                pago.getImportePagado(),
                pago.getInteres()
        );
    }

    private void agregarLinks(Pago pago, EntityModel<PagoResponseDTO> recurso) throws Excepcion {
        recurso.add(linkTo(methodOn(PagoRestController.class)
            .getById(pago.getId())).withSelfRel());
        recurso.add(linkTo(methodOn(FacturaRestController.class)
            .getById(pago.getFactura().getId())).withRel("factura"));
    }
}
