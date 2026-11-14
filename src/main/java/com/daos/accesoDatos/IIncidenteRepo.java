package com.daos.accesoDatos;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daos.entidades.Incidente;
import com.daos.entidades.CategoriaIncidente;
import com.daos.entidades.EstadoIncidente;

import com.daos.entidades.PrioridadIncidente;


@Repository 
public interface IIncidenteRepo extends JpaRepository <Incidente, Long>{
@Query("""
		SELECT i
		FROM Incidente i
		WHERE (:contratoId IS NULL OR i.contrato.id = :contratoId)
		  AND (:estado IS NULL OR i.estado = :estado)
		  AND (:categoria IS NULL OR i.categoria = :categoria)
		  AND (:prioridad IS NULL OR i.prioridad = :prioridad)
		ORDER BY i.id
	""")

    List<Incidente> filtrar(@Param("contratoId") Long contratoId, @Param("estado") EstadoIncidente estado,
			@Param("categoria") CategoriaIncidente categoria, @Param("prioridad") PrioridadIncidente prioridad);
    
    
}
