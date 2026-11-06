package com.daos.presentacion.facturas;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daos.entidades.Factura;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class FacturaForm {

	public static final String FORMATO_PERIODO = "\\d{4}-(0[1-9]|1[0-2])";

	@NotNull(message = "El contrato es obligatorio")
	private Long contratoId;

	@NotNull(message = "El período es obligatorio")
	@Pattern(regexp = FORMATO_PERIODO, message = "El período debe tener el formato AAAA-MM")
	private String periodo;

	@Size(max = 255, message = "El concepto no puede superar los 255 caracteres")
	private String concepto;

	private LocalDate fechaEmision;

	private LocalDate fechaVencimiento;

	@Positive(message = "El importe debe ser mayor a cero")
	private BigDecimal importe;

	public FacturaForm() {
		super();
	}

	public Factura toPojo() {
		Factura f = new Factura();
		f.setPeriodo(this.periodo);
		f.setConcepto(this.concepto);
		f.setFechaEmision(this.fechaEmision);
		f.setFechaVencimiento(this.fechaVencimiento);
		f.setImporte(this.importe);
		return f;
	}

	public Long getContratoId() {
		return contratoId;
	}

	public void setContratoId(Long contratoId) {
		this.contratoId = contratoId;
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
}
