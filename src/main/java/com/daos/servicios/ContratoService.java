package com.daos.servicios;

import com.daos.entidades.Contrato;
import com.daos.excepciones.Excepcion;

public interface ContratoService {

	Contrato getById(Long id) throws Excepcion;
}
