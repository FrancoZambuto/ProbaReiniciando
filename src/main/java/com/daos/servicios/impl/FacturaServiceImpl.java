package com.daos.servicios.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daos.accesoDatos.IFacturaRepo;
import com.daos.entidades.Contrato;
import com.daos.entidades.EstadoContrato;
import com.daos.entidades.EstadoFactura;
import com.daos.entidades.Factura;
import com.daos.entidades.TipoFactura;
import com.daos.excepciones.Excepcion;
import com.daos.presentacion.facturas.FacturaForm;
import com.daos.servicios.ContratoService;
import com.daos.servicios.FacturaService;

@Service
public class FacturaServiceImpl implements FacturaService {

	@Autowired
	private IFacturaRepo repo;

	@Autowired
	private ContratoService contratoService;

	@Override
	@Transactional
	public Factura emitir(Factura factura, Long contratoId) throws Excepcion {

		Contrato contrato = contratoService.getById(contratoId);
		if (contrato.getEstado() != EstadoContrato.ACTIVO)
			throw new Excepcion("El contrato " + contratoId + " no se encuentra activo (estado: " + contrato.getEstado() + ")", 409);

		YearMonth periodo = YearMonth.parse(factura.getPeriodo());
		LocalDate inicioVigencia = contrato.getFechaInicio();
		LocalDate finVigencia = contrato.getFechaFinVigencia();
		if (periodo.atEndOfMonth().isBefore(inicioVigencia) || periodo.atDay(1).isAfter(finVigencia))
			throw new Excepcion("El período " + periodo + " no está comprendido en la vigencia del contrato ("
					+ inicioVigencia + " a " + finVigencia + ")", 409);

		if (factura.getImporte() == null) {
			if (repo.existeMensualNoAnulada(contratoId, factura.getPeriodo()))
				throw new Excepcion("Ya existe una factura de facturación mensual no anulada para el contrato "
						+ contratoId + " y el período " + periodo, 409);
			factura.setTipo(TipoFactura.MENSUAL);
			factura.setImporte(contrato.getImporteMensual());
		} else {
			factura.setTipo(TipoFactura.ADICIONAL);
		}

		if (factura.getFechaEmision() == null)
			factura.setFechaEmision(LocalDate.now());

		if (factura.getFechaVencimiento() == null)
			factura.setFechaVencimiento(calcularVencimiento(periodo, contrato.getDiaVencimientoMensual()));

		if (factura.getFechaEmision().isAfter(factura.getFechaVencimiento()))
			throw new Excepcion("La fecha de emisión (" + factura.getFechaEmision()
					+ ") no puede ser posterior a la fecha de vencimiento (" + factura.getFechaVencimiento() + ")", 409);

		factura.setContrato(contrato);
		factura.setEstado(EstadoFactura.PENDIENTE);
		return repo.save(factura);
	}

	@Override
	public Factura getById(Long id) throws Excepcion {
		return repo.findById(id).orElseThrow(() -> new Excepcion("No existe la factura con id=" + id, 404));
	}

	@Override
	public List<Factura> filtrar(Long contratoId, String periodo, EstadoFactura estado, TipoFactura tipo)
			throws Excepcion {
		if (periodo != null && !periodo.matches(FacturaForm.FORMATO_PERIODO))
			throw new Excepcion("periodo: El período debe tener el formato AAAA-MM", 400);

		List<Factura> facturas = repo.filtrar(contratoId, periodo, tipo);
		if (estado == null)
			return facturas;

		LocalDate hoy = LocalDate.now();
		List<Factura> resultado = new ArrayList<Factura>();
		for (Factura f : facturas) {
			if (f.getEstadoActual(hoy) == estado)
				resultado.add(f);
		}
		return resultado;
	}

	@Override
	@Transactional
	public Factura cambiarEstado(Long id, EstadoFactura nuevoEstado) throws Excepcion {
		Factura factura = getById(id);

		if (nuevoEstado == EstadoFactura.PAGADA)
			throw new Excepcion("El estado pagada solamente puede establecerse mediante el registro de un pago", 409);
		if (nuevoEstado != EstadoFactura.ANULADA)
			throw new Excepcion("Transición de estado no permitida: este servicio solo permite anular facturas", 409);

		EstadoFactura estadoActual = factura.getEstadoActual(LocalDate.now());
		if (estadoActual != EstadoFactura.PENDIENTE && estadoActual != EstadoFactura.VENCIDA)
			throw new Excepcion("Solo pueden anularse facturas pendientes o vencidas. La factura " + id
					+ " se encuentra " + estadoActual, 409);

		factura.setEstado(EstadoFactura.ANULADA);
		return repo.save(factura);
	}

	private LocalDate calcularVencimiento(YearMonth periodo, Integer diaVencimiento) {
		int dia = Math.min(diaVencimiento, periodo.lengthOfMonth());
		return periodo.atDay(dia);
	}
}
