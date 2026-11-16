package com.daos.presentacion.pagos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daos.entidades.MedioPago;

public class PagoResponseDTO {

    private Long id;
    private Long facturaId;
    private LocalDate fechaPago;
    private MedioPago medioPago;
    private BigDecimal importePagado;
    private BigDecimal interes;

    public PagoResponseDTO() {
    }

    public PagoResponseDTO(Long id, Long facturaId,
                           LocalDate fechaPago, MedioPago medioPago,
                           BigDecimal importePagado, BigDecimal interes) {
        this.id = id;
        this.facturaId = facturaId;
        this.fechaPago = fechaPago;
        this.medioPago = medioPago;
        this.importePagado = importePagado;
        this.interes = interes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Long facturaId) {
        this.facturaId = facturaId;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public BigDecimal getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(BigDecimal importePagado) {
        this.importePagado = importePagado;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }
}
