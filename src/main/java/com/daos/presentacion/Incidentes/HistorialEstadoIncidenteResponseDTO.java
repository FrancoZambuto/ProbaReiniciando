package com.daos.presentacion.Incidentes;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.HistorialEstadoIncidente;

public class HistorialEstadoIncidenteResponseDTO extends RepresentationModel<HistorialEstadoIncidenteResponseDTO> {

	private Long id;
	private Long incidenteId;
	private EstadoIncidente estadoAnterior;
	private EstadoIncidente estadoNuevo;
	private LocalDateTime fechaHora;

	public HistorialEstadoIncidenteResponseDTO(HistorialEstadoIncidente pojo) {
		super();
		this.id = pojo.getId();
		this.incidenteId = pojo.getIncidente().getId();
		this.estadoAnterior = pojo.getEstadoAnterior();
		this.estadoNuevo = pojo.getEstadoNuevo();
		this.fechaHora = pojo.getFechaHora();
	}

	public Long getId() {
		return id;
	}

	public Long getIncidenteId() {
		return incidenteId;
	}

	public EstadoIncidente getEstadoAnterior() {
		return estadoAnterior;
	}

	public EstadoIncidente getEstadoNuevo() {
		return estadoNuevo;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
}