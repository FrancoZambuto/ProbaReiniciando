package com.daos.presentacion.Incidentes;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.PrioridadIncidente;

public class IncidenteResponseDTO extends RepresentationModel<IncidenteResponseDTO> {

	private Long id;
	private Long contratoId;
	private String titulo;
	private String descripcion;
	private CategoriaIncidente categoria;
	private PrioridadIncidente prioridad;
	private LocalDateTime fechaAlta;
	private EstadoIncidente estado;

	public IncidenteResponseDTO(Incidente pojo) {
		super();
		this.id = pojo.getId();
		this.contratoId = pojo.getContrato().getId();
		this.titulo = pojo.getTitulo();
		this.descripcion = pojo.getDescripcion();
		this.categoria = pojo.getCategoria();
		this.prioridad = pojo.getPrioridad();
		this.fechaAlta = pojo.getFechaALta();
		this.estado = pojo.getEstado();
	}

	public Long getId() {
		return id;
	}

	public Long getContratoId() {
		return contratoId;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public CategoriaIncidente getCategoria() {
		return categoria;
	}

	public PrioridadIncidente getPrioridad() {
		return prioridad;
	}

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public EstadoIncidente getEstado() {
		return estado;
	}
}