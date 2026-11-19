package com.daos.presentacion.Incidentes;

import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.PrioridadIncidente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IncidenteForm {
    @NotNull (message = "El contrato es obligatorio")
	private Long contratoId;

	@NotBlank(message = "El título es obligatorio")
	@Size(max = 255, message = "El título no puede superar los 255 caracteres")
	private String titulo;

	@NotBlank(message = "La descripción es obligatoria")
	@Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
	private String descripcion;

	@NotNull(message = "La categoría es obligatoria")
	private CategoriaIncidente categoria;

	@NotNull(message = "La prioridad es obligatoria")
	private PrioridadIncidente prioridad;

	public IncidenteForm() {
		super();
	}

	public Incidente toPojo() {
		Incidente i = new Incidente();
		i.setTitulo(this.titulo);
		i.setDescripcion(this.descripcion);
		i.setCategoria(this.categoria);
		i.setPrioridad(this.prioridad);
		return i;
	}

	public Long getContratoId() {
		return contratoId;
	}

	public void setContratoId(Long contratoId) {
		this.contratoId = contratoId;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public CategoriaIncidente getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaIncidente categoria) {
		this.categoria = categoria;
	}

	public PrioridadIncidente getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(PrioridadIncidente prioridad) {
		this.prioridad = prioridad;
	}
}
    

