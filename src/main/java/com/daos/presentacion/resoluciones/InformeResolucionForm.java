package com.daos.presentacion.resoluciones;
import java.math.BigDecimal;
import com.daos.entidades.InformeResolucion;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class InformeResolucionForm {
    @NotBlank(message = "La descripción del trabajo es obligatoria")
    @Size(max = 1000, message = "La descripción admite hasta 1000 caracteres")
    private String descripcionTrabajo;

    @NotBlank(message = "El responsable es obligatorio")
    @Size(max = 255, message = "El responsable admite hasta 255 caracteres")
    private String responsable;

    @NotNull(message = "El costo es obligatorio")
    @PositiveOrZero(message = "El costo debe ser mayor o igual a cero")
    @Digits(integer = 10, fraction = 2, message = "El costo admite hasta 10 dígitos enteros y 2 decimales")
    private BigDecimal costo;

    @Size(max = 1000, message = "Las observaciones admiten hasta 1000 caracteres")
    private String observaciones;

    public InformeResolucionForm() {
    }

    public String getDescripcionTrabajo() {
        return descripcionTrabajo;
    }

    public void setDescripcionTrabajo(String descripcionTrabajo) {
        this.descripcionTrabajo = descripcionTrabajo;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

    public InformeResolucion toPojo() {
        InformeResolucion informe = new InformeResolucion();
        informe.setDescripcionTrabajo(descripcionTrabajo);
        informe.setResponsable(responsable);
        informe.setCosto(costo);
        informe.setObservaciones(observaciones);
        return informe;
    }
    
    @NotNull(message = "El incidente es obligatorio")
    private Long incidenteId;

    public Long getIncidenteId() {
        return incidenteId;
    }

    public void setIncidenteId(Long incidenteId) {
        this.incidenteId = incidenteId;
    }
}