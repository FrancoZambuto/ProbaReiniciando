package com.daos.servicios.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import com.daos.accesoDatos.IFacturaRepo;
import com.daos.accesoDatos.IPagoRepo;
import com.daos.entidades.EstadoFactura;
import com.daos.entidades.Factura;
import com.daos.entidades.Pago;
import com.daos.excepciones.Excepcion;
import com.daos.servicios.PagoService;

@Service
public class PagoServiceImpl implements PagoService {

    private final IPagoRepo pagoRepo;
    private final IFacturaRepo facturaRepo;

    public PagoServiceImpl(IPagoRepo pagoRepo, IFacturaRepo facturaRepo) {
        this.pagoRepo = pagoRepo;
        this.facturaRepo = facturaRepo;
    }

    @Override
@Transactional(rollbackFor = Excepcion.class)
public Pago registrar(Pago pago, Long facturaId) throws Excepcion {

    if (pago == null) {
        throw new Excepcion("Los datos del pago son obligatorios.", 400);
    }

    Factura factura = facturaRepo.findById(facturaId)
            .orElseThrow(() -> new Excepcion("No se encontró la factura.", 404));

    EstadoFactura estadoActual = factura.getEstadoActual(LocalDate.now());

    if (estadoActual != EstadoFactura.PENDIENTE
            && estadoActual != EstadoFactura.VENCIDA) {
        throw new Excepcion(
                "La factura no se encuentra en un estado que permita realizar el pago.",
                409);
    }

    if (pagoRepo.existsByFacturaId(facturaId)) {
        throw new Excepcion("La factura ya tiene un pago registrado.", 409);
    }

    if (pago.getMedioPago() == null
            || pago.getImportePagado() == null
            || pago.getInteres() == null) {
        throw new Excepcion("Todos los datos del pago son obligatorios.", 400);
    }

    if (pago.getFechaPago() == null) {
        pago.setFechaPago(LocalDate.now());
    }

    LocalDate hoy = LocalDate.now();

    if (pago.getFechaPago().isAfter(hoy)) {
        throw new Excepcion("La fecha de pago no puede ser futura.", 400);
    }

    if (factura.getFechaEmision() == null
            || pago.getFechaPago().isBefore(factura.getFechaEmision())) {
        throw new Excepcion(
                "La fecha de pago debe ser igual o posterior a la fecha de emisión de la factura.",
                400);
    }

    if (pago.getImportePagado().signum() <= 0) {
        throw new Excepcion("El importe pagado debe ser mayor a cero.", 400);
    }

    if (pago.getInteres().signum() < 0) {
        throw new Excepcion("El interés no puede ser negativo.", 400);
    }

    if (factura.getImporte() == null || factura.getImporte().signum() <= 0) {
        throw new Excepcion("El importe de la factura no es válido.", 400);
    }

    BigDecimal total = factura.getImporte().add(pago.getInteres());

    if (pago.getImportePagado().compareTo(total) != 0) {
        throw new Excepcion(
                "El importe pagado debe coincidir con el importe de la factura más los intereses.",
                400);
    }

    pago.setFactura(factura);

    try {
        Pago pagoRegistrado = pagoRepo.saveAndFlush(pago);

        factura.setEstado(EstadoFactura.PAGADA);
        facturaRepo.save(factura);

        return pagoRegistrado;

    } catch (DataIntegrityViolationException e) {
        throw new Excepcion("La factura ya tiene un pago registrado.", 409);
    }
}

    @Override
    @Transactional(readOnly = true)
    public Pago getById(Long id) throws Excepcion {
        return pagoRepo.findById(id)
                .orElseThrow(() ->
                    new Excepcion("No se encontró el pago.", 404));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> filtrar(Long facturaId) throws Excepcion {
        if (facturaId == null) {
            return pagoRepo.findAll();
        }
        return pagoRepo.findByFacturaId(facturaId);
    }
}
