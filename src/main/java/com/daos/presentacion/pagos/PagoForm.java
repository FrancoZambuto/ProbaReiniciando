
package com.daos.formularios;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.daos.entidades.MedioPago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class PagoForm {

    @NotNull(message = "El ID de la factura es obligatorio")
    private Long facturaId;

    private LocalDate fechaPago;

    @NotNull(message = "El medio de pago es obligatorio.")
    private MedioPago medioPago;

    @NotNull(message = "El importe pagado es obligatorio.")
    @Positive(message = "El importe pagado debe ser mayor a cero.")
    private BigDecimal importePagado;

    @NotNull(message = "El interés es obligatorio.")
    @DecimalMin(value = "0.00", message = "El interés no puede ser negativo.")
    private BigDecimal interes;

    public PagoForm() {
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
