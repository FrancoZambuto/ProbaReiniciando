package com.daos.presentacion.resoluciones;

import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.daos.entidades.InformeResolucion;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.Incidentes.IncidenteRestController;
import com.daos.servicios.InformeResolucionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Informes de resolución")
public class InformeResolucionRestController {

    private final InformeResolucionService service;

    public InformeResolucionRestController(InformeResolucionService service) {
        this.service = service;
    }

    @Operation(summary = "Registra el informe y resuelve un incidente en proceso")
    @ApiResponse(responseCode = "201", description = "Informe registrado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Incidente inexistente")
    @ApiResponse(responseCode = "409", description = "El incidente no está en proceso")
    @PostMapping("/incidentes/{id}/resolucion")
    public ResponseEntity<InformeResolucionResponseDTO> resolver(
            @PathVariable("id") Long incidenteId,
            @Valid @RequestBody InformeResolucionForm form) throws Excepcion {

        InformeResolucion pojo = service.resolver(incidenteId, form.toPojo());
        InformeResolucionResponseDTO dto = buildResponse(pojo);

        return ResponseEntity.created(dto.getRequiredLink("self").toUri())
                .body(dto);
    }

    @Operation(summary = "Consulta un informe por su ID")
    @GetMapping("/informes-resolucion/{id}")
    public ResponseEntity<InformeResolucionResponseDTO> getById(
            @PathVariable("id") Long id) throws Excepcion {

        InformeResolucion pojo = service.getById(id);
        return ResponseEntity.ok(buildResponse(pojo));
    }

    @Operation(summary = "Lista informes con un filtro opcional por incidente")
    @GetMapping("/informes-resolucion")
    public List<InformeResolucionResponseDTO> filtrar(
            @RequestParam(name = "incidenteId", required = false) Long incidenteId)
            throws Excepcion {

        List<InformeResolucionResponseDTO> informes = new ArrayList<>();
        for (InformeResolucion pojo : service.filtrar(incidenteId)) {
            informes.add(buildResponse(pojo));
        }
        return informes;
    }

    private InformeResolucionResponseDTO buildResponse(InformeResolucion pojo)
            throws Excepcion {

        InformeResolucionResponseDTO dto = new InformeResolucionResponseDTO(pojo);

        Link selfLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(InformeResolucionRestController.class)
                        .getById(pojo.getId()))
                .withSelfRel();

        Link incidenteLink = WebMvcLinkBuilder.linkTo(IncidenteRestController.class)
                .slash(pojo.getIncidente().getId())
                .withRel("incidente");

        dto.add(selfLink);
        dto.add(incidenteLink);

        return dto;
    }
}