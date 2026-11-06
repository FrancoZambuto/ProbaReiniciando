package com.daos.presentacion.contratos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.daos.entidades.Contrato;
import com.daos.entidades.EstadoContrato;

public class ContratoResponseDTO extends RepresentationModel<ContratoResponseDTO> {

	private Long id;
	private Long propiedadId;
	private Long propietarioId;
	private Long inquilinoId;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Integer duracionMeses;
	private BigDecimal importeMensual;
	private Integer diaVencimientoMensual;
	private String descripcion;
	private EstadoContrato estado;

	public ContratoResponseDTO(Contrato pojo) {
		super();
		this.id = pojo.getId();
		this.propiedadId = pojo.getPropiedad() != null ? pojo.getPropiedad().getId() : null;
		this.propietarioId = pojo.getPropietario() != null ? pojo.getPropietario().getId() : null;
		this.inquilinoId = pojo.getInquilino() != null ? pojo.getInquilino().getId() : null;
		this.fechaInicio = pojo.getFechaInicio();
		this.fechaFin = pojo.getFechaFinVigencia();
		this.duracionMeses = pojo.getDuracionMeses();
		this.importeMensual = pojo.getImporteMensual();
		this.diaVencimientoMensual = pojo.getDiaVencimientoMensual();
		this.descripcion = pojo.getDescripcion();
		this.estado = pojo.getEstado();
	}

	public Long getId() {
		return id;
	}

	public Long getPropiedadId() {
		return propiedadId;
	}

	public Long getPropietarioId() {
		return propietarioId;
	}

	public Long getInquilinoId() {
		return inquilinoId;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public Integer getDuracionMeses() {
		return duracionMeses;
	}

	public BigDecimal getImporteMensual() {
		return importeMensual;
	}

	public Integer getDiaVencimientoMensual() {
		return diaVencimientoMensual;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public EstadoContrato getEstado() {
		return estado;
	}
}
