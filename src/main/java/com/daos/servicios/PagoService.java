package com.daos.servicios;

import java.util.List;

import com.daos.entidades.Pago;
import com.daos.excepciones.Excepcion;

public interface PagoService {

    Pago registrar(Pago pago, Long facturaId) throws Excepcion;

    Pago getById(Long id) throws Excepcion;

    List<Pago> filtrar(Long facturaId) throws Excepcion;
}