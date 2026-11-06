package com.daos.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FACTURA")
public class Factura {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "contrato_id", nullable = false)
	private Contrato contrato;

	@Column(length = 7, nullable = false)
	private String periodo;

	private String concepto;

	private LocalDate fechaEmision;

	private LocalDate fechaVencimiento;

	@Column(precision = 12, scale = 2)
	private BigDecimal importe;

	@Enumerated(EnumType.STRING)
	private TipoFactura tipo;

	@Enumerated(EnumType.STRING)
	private EstadoFactura estado;

	public Factura() {
	}

	public EstadoFactura getEstadoActual(LocalDate hoy) {
		if (estado == EstadoFactura.PENDIENTE && hoy.isAfter(fechaVencimiento))
			return EstadoFactura.VENCIDA;
		return estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public LocalDate getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(LocalDate fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public TipoFactura getTipo() {
		return tipo;
	}

	public void setTipo(TipoFactura tipo) {
		this.tipo = tipo;
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}
}
