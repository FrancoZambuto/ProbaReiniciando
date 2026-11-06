package com.daos.servicios.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daos.accesoDatos.IContratoRepo;
import com.daos.entidades.Contrato;
import com.daos.excepciones.Excepcion;
import com.daos.servicios.ContratoService;

@Service
public class ContratoServiceImpl implements ContratoService {

	@Autowired
	private IContratoRepo repo;

	@Override
	public Contrato getById(Long id) throws Excepcion {
		Contrato contrato = repo.findById(id).orElse(null);
		if (contrato == null || contrato.isEliminado())
			throw new Excepcion("No existe el contrato con id=" + id, 404);
		return contrato;
	}
}
