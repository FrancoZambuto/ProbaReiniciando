package com.daos.presentacion.resoluciones;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;
import com.daos.entidades.InformeResolucion;

public class InformeResolucionResponseDTO
        extends RepresentationModel<InformeResolucionResponseDTO> {

    private Long id;
    private Long incidenteId;
    private String descripcionTrabajo;
    private LocalDateTime fechaHoraResolucion;
    private BigDecimal costo;
    private String observaciones;
    private String responsable;

    public InformeResolucionResponseDTO() {
    }

    public InformeResolucionResponseDTO(InformeResolucion pojo) {
        this.id = pojo.getId();
        this.incidenteId = pojo.getIncidente().getId();
        this.descripcionTrabajo = pojo.getDescripcionTrabajo();
        this.fechaHoraResolucion = pojo.getFechaHoraResolucion();
        this.costo = pojo.getCosto();
        this.observaciones = pojo.getObservaciones();
        this.responsable = pojo.getResponsable();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidenteId() {
        return incidenteId;
    }

    public void setIncidenteId(Long incidenteId) {
        this.incidenteId = incidenteId;
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public void setDescripcionTrabajo(String descripcionTrabajo) {
        this.descripcionTrabajo = descripcionTrabajo;
    }

    public LocalDateTime getFechaHoraResolucion() {
        return fechaHoraResolucion;
    }

    public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) {
        this.fechaHoraResolucion = fechaHoraResolucion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}