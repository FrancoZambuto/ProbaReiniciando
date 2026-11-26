package com.daos.servicios.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.daos.accesoDatos.IInformeResolucionRepo;
import com.daos.entidades.EstadoIncidente;
import com.daos.entidades.Incidente;
import com.daos.entidades.InformeResolucion;
import com.daos.excepciones.Excepcion;
import com.daos.servicios.IncidenteService;
import com.daos.servicios.InformeResolucionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Service
public class InformeResolucionServiceImpl implements InformeResolucionService {

	@Autowired
	private IInformeResolucionRepo repo;
	@Autowired
	private IncidenteService incidenteService;
	@PersistenceContext
	private EntityManager entityManager;
	@Override
	@Transactional(rollbackFor = Excepcion.class)
	public InformeResolucion resolver(Long incidenteId, InformeResolucion informe) throws Excepcion {

		// 1. Buscar el incidente
		Incidente incidente = entityManager.find(Incidente.class, incidenteId, LockModeType.PESSIMISTIC_WRITE);
		if (incidente == null) {
			throw new Excepcion("No existe el incidente con id=" + incidenteId, 404);
		}

		// 2. Verificar su estado
		if (incidente.getEstado() != EstadoIncidente.EN_PROCESO) {
			throw new Excepcion("El incidente debe estar EN_PROCESO", 409);
		}

		// 3. Validar los datos del informe
		if (informe == null) {
			throw new Excepcion("El informe es obligatorio", 400);
		}

		if (informe.getDescripcionTrabajo() == null || informe.getDescripcionTrabajo().isBlank()
				|| informe.getDescripcionTrabajo().length() > 1000) {
			throw new Excepcion("La descripción del trabajo es obligatoria" + " y no puede superar 1000 caracteres", 400);
		}

		if (informe.getResponsable() == null || informe.getResponsable().isBlank()) {
			throw new Excepcion("El responsable es obligatorio", 400);
		}

		if (informe.getCosto() == null || informe.getCosto().signum() < 0) {
			throw new Excepcion("El costo debe ser mayor o igual a cero", 400);
		}

		// 4. Generar fecha y hora
	    LocalDateTime fechaHora = LocalDateTime.now();

	    informe.setId(null);
	    informe.setIncidente(incidente);
	    informe.setFechaHoraResolucion(fechaHora);

	    // 5. Cambiar estado y registrar historial
	    incidenteService.marcarResuelto(incidenteId, fechaHora);

	    // 6. Guardar el informe
	    return repo.save(informe);
	}

	@Override
	public InformeResolucion getById(Long id) throws Excepcion {
		return repo.findById(id).orElseThrow(() -> new Excepcion("No existe el informe con ID " + id, 404));
	}

	@Override
	public List<InformeResolucion> filtrar(Long incidenteId) throws Excepcion {
		if (incidenteId == null) { return repo.findAll(); }
		incidenteService.getById(incidenteId);
		return repo.findByIncidenteId(incidenteId);
	}
}