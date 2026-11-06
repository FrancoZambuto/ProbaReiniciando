package com.daos.presentacion.facturas;

import com.daos.entidades.EstadoFactura;

import jakarta.validation.constraints.NotNull;

public class EstadoFacturaForm {

	@NotNull(message = "El estado es obligatorio")
	private EstadoFactura estado;

	public EstadoFacturaForm() {
		super();
	}

	public EstadoFactura getEstado() {
		return estado;
	}

	public void setEstado(EstadoFactura estado) {
		this.estado = estado;
	}
}
