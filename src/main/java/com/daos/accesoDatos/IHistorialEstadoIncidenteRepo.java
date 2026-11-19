package com.daos.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daos.entidades.HistorialEstadoIncidente;

@Repository 
public interface IHistorialEstadoIncidenteRepo extends JpaRepository <HistorialEstadoIncidente, Long> {

	@Query("""
		SELECT h
		FROM HistorialEstadoIncidente h
		WHERE h.incidente.id = :incidenteId
		ORDER BY h.fechaHora, h.id
	""")
	List<HistorialEstadoIncidente> buscarPorIncidente(@Param("incidenteId") Long incidenteId);
}
    

