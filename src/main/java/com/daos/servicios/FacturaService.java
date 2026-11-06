package com.daos.servicios;

import java.util.List;

import com.daos.entidades.EstadoFactura;
import com.daos.entidades.Factura;
import com.daos.entidades.TipoFactura;
import com.daos.excepciones.Excepcion;

public interface FacturaService {

	Factura emitir(Factura factura, Long contratoId) throws Excepcion;

	Factura getById(Long id) throws Excepcion;

	List<Factura> filtrar(Long contratoId, String periodo, EstadoFactura estado, TipoFactura tipo) throws Excepcion;

	Factura cambiarEstado(Long id, EstadoFactura nuevoEstado) throws Excepcion;
}
