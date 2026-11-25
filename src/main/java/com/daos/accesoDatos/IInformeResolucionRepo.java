package com.daos.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daos.entidades.InformeResolucion;

@Repository
public interface IInformeResolucionRepo
        extends JpaRepository<InformeResolucion, Long> {

    List<InformeResolucion> findByIncidenteId(Long incidenteId);
}