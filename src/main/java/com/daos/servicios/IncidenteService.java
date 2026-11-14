package com.daos.servicios;

import java.time.LocalDateTime;
import java.util.List;

import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.HistorialEstadoIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.PrioridadIncidente;
import com.daos.excepciones.Excepcion;

public interface IncidenteService {

	Incidente crear(Incidente incidente, Long contratoId) throws Excepcion;

	Incidente getById(Long id) throws Excepcion;

	List<Incidente> filtrar(Long contratoId, EstadoIncidente estado, CategoriaIncidente categoria,
			PrioridadIncidente prioridad) throws Excepcion;

	Incidente cambiarEstado(Long id, EstadoIncidente nuevoEstado) throws Excepcion;

	Incidente marcarResuelto(Long id, LocalDateTime fechaHora) throws Excepcion;

	List<HistorialEstadoIncidente> getHistorial(Long id) throws Excepcion;
}
