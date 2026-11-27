package com.daos.servicios.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daos.accesoDatos.IHistorialEstadoIncidenteRepo;
import com.daos.accesoDatos.IIncidenteRepo;
import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.Contrato;
import com.daos.entidades.EstadoContrato;
import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.HistorialEstadoIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.PrioridadIncidente;
import com.daos.excepciones.Excepcion;
import com.daos.servicios.ContratoService;
import com.daos.servicios.IncidenteService;

@Service 
public class IncidenteServiceImpl implements IncidenteService {
    @Autowired 
    private IIncidenteRepo repo;

    @Autowired 
    private IHistorialEstadoIncidenteRepo historialRepo;

    @Autowired 
    private ContratoService contratoService;
    @Override
	@Transactional(rollbackFor = Excepcion.class)
	public Incidente crear(Incidente incidente, Long contratoId) throws Excepcion {

		Contrato contrato = contratoService.getById(contratoId);
		if (contrato.isEliminado())
			throw new Excepcion("El contrato " + contratoId + " está eliminado", 409);
		if (contrato.getEstado() != EstadoContrato.ACTIVO)
			throw new Excepcion(
					"El contrato " + contratoId + " no se encuentra activo (estado: " + contrato.getEstado() + ")", 409);

		LocalDate hoy = LocalDate.now();
		LocalDate inicioVigencia = contrato.getFechaInicio();
		LocalDate finVigencia = contrato.getFechaFinVigencia();
		if (hoy.isBefore(inicioVigencia) || hoy.isAfter(finVigencia))
			throw new Excepcion("El contrato " + contratoId + " no se encuentra vigente en la fecha de alta ("
					+ inicioVigencia + " a " + finVigencia + ")", 409);

		LocalDateTime ahora = LocalDateTime.now();
		incidente.setContrato(contrato);
		incidente.setFechaALta(ahora);
		incidente.setEstado(EstadoIncidente.ABIERTO);
		Incidente guardado = repo.save(incidente);

		registrarHistorial(guardado, null, EstadoIncidente.ABIERTO, ahora);
		return guardado;
	}

	@Override
	public Incidente getById(Long id) throws Excepcion {
		return repo.findById(id).orElseThrow(() -> new Excepcion("No existe el incidente con id=" + id, 404));
	}

	@Override
	public List<Incidente> filtrar(Long contratoId, EstadoIncidente estado, CategoriaIncidente categoria,
			PrioridadIncidente prioridad) throws Excepcion {
		return repo.filtrar(contratoId, estado, categoria, prioridad);
	}

	@Override
	@Transactional(rollbackFor = Excepcion.class)  //modificado por ariel
	public Incidente cambiarEstado(Long id, EstadoIncidente nuevoEstado) throws Excepcion {
		Incidente incidente = getById(id);

		if (nuevoEstado == EstadoIncidente.RESUELTO)
			throw new Excepcion("La resolución de un incidente se realiza mediante un informe de resolución", 409);

		EstadoIncidente actual = incidente.getEstado();
		if (actual == EstadoIncidente.CANCELADO)
			throw new Excepcion("Los incidentes cancelados no admiten nuevas transiciones", 409);
		if (!transicionPermitida(actual, nuevoEstado))
			throw new Excepcion("Transición de estado no permitida: de " + actual + " a " + nuevoEstado, 409);

		return aplicarCambio(incidente, nuevoEstado, LocalDateTime.now()).getIncidente();
	}

	@Override
	@Transactional(rollbackFor = Excepcion.class)
	public HistorialEstadoIncidente marcarResuelto(Long id, LocalDateTime fechaHora)
	        throws Excepcion {
	    Incidente incidente = getById(id);

	    if (incidente.getEstado() != EstadoIncidente.EN_PROCESO) {
	        throw new Excepcion(
	                "Solo pueden resolverse incidentes en proceso. El incidente "
	                        + id + " se encuentra " + incidente.getEstado(),409);
	    }

	    return aplicarCambio(incidente, EstadoIncidente.RESUELTO, fechaHora);
	}

	@Override
	public List<HistorialEstadoIncidente> getHistorial(Long id) throws Excepcion {
		getById(id);
		return historialRepo.buscarPorIncidente(id);
	}

	private HistorialEstadoIncidente aplicarCambio( Incidente incidente, EstadoIncidente nuevoEstado, LocalDateTime fechaHora) {
	    EstadoIncidente anterior = incidente.getEstado();
	    incidente.setEstado(nuevoEstado);
	    Incidente guardado = repo.save(incidente);
	    return registrarHistorial(guardado, anterior, nuevoEstado, fechaHora);
	}

	private HistorialEstadoIncidente registrarHistorial(Incidente incidente, EstadoIncidente anterior, EstadoIncidente nuevo, LocalDateTime fechaHora) { //modificado por ariel
		HistorialEstadoIncidente h = new HistorialEstadoIncidente();
		h.setIncidente(incidente);
		h.setEstadoAnterior(anterior);
		h.setEstadoNuevo(nuevo);
		h.setFechaHora(fechaHora);
		return historialRepo.save(h);
	}

	private boolean transicionPermitida(EstadoIncidente actual, EstadoIncidente nuevo) {
		switch (actual) {
		case ABIERTO:
			return nuevo == EstadoIncidente.EN_PROCESO || nuevo == EstadoIncidente.CANCELADO;
		case EN_PROCESO:
			return nuevo == EstadoIncidente.CANCELADO;
		case RESUELTO:
			return nuevo == EstadoIncidente.REABIERTO;
		case REABIERTO:
			return nuevo == EstadoIncidente.EN_PROCESO;
		default:
			return false;
		}
	}
}


