package com.daos.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.daos.entidades.EstadoFactura;
import com.daos.entidades.Factura;
import com.daos.entidades.TipoFactura;

public class FacturaResponseDTO extends RepresentationModel<FacturaResponseDTO> {

	private Long id;
	private Long contratoId;
	private String periodo;
	private String concepto;
	private LocalDate fechaEmision;
	private LocalDate fechaVencimiento;
	private BigDecimal importe;
	private TipoFactura tipo;
	private EstadoFactura estado;

	public FacturaResponseDTO(Factura pojo) {
		super();
		this.id = pojo.getId();
		this.contratoId = pojo.getContrato().getId();
		this.periodo = pojo.getPeriodo();
		this.concepto = pojo.getConcepto();
		this.fechaEmision = pojo.getFechaEmision();
		this.fechaVencimiento = pojo.getFechaVencimiento();
		this.importe = pojo.getImporte();
		this.tipo = pojo.getTipo();
		this.estado = pojo.getEstadoActual(LocalDate.now());
	}

	public Long getId() {
		return id;
	}

	public Long getContratoId() {
		return contratoId;
	}

	public String getPeriodo() {
		return periodo;
	}

	public String getConcepto() {
		return concepto;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public TipoFactura getTipo() {
		return tipo;
	}

	public EstadoFactura getEstado() {
		return estado;
	}
}
