package com.daos.presentacion.Incidentes;
import com.daos.entidades.EstadoIncidente;

import jakarta.validation.constraints.NotNull;

public class IncidenteEstadoForm {

	@NotNull(message = "El estado es obligatorio")
	private EstadoIncidente estado;

	public IncidenteEstadoForm() {
		super();
	}

	public EstadoIncidente getEstado() {
		return estado;
	}

	public void setEstado(EstadoIncidente estado) {
		this.estado = estado;
	}
}

    
