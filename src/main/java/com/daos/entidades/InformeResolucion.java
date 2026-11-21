package com.daos.entidades;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "INFORME_RESOLUCION")
public class InformeResolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "incidente_id", nullable = false)
    private Incidente incidente;

    @Column(nullable = false, length = 1000)
    private String descripcionTrabajo;

    @Column(nullable = false)
    private LocalDateTime fechaHoraResolucion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @Column(length = 1000)
    private String observaciones;

    @Column(nullable = false)
    private String responsable;

    public InformeResolucion() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Incidente getIncidente() {
		return incidente;
	}

	public void setIncidente(Incidente incidente) {
		this.incidente = incidente;
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
