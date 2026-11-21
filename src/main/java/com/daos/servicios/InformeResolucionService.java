package com.daos.servicios;

import java.util.List;
import com.daos.entidades.InformeResolucion;
import com.daos.excepciones.Excepcion;

public interface InformeResolucionService {

    InformeResolucion resolver(
        Long incidenteId,
        InformeResolucion informe
    ) throws Excepcion;

    InformeResolucion getById(Long id)
        throws Excepcion;

    List<InformeResolucion> filtrar(
        Long incidenteId
    ) throws Excepcion;
}