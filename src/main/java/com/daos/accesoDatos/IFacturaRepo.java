package com.daos.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daos.entidades.Factura;
import com.daos.entidades.TipoFactura;

@Repository
public interface IFacturaRepo extends JpaRepository<Factura, Long> {

	@Query("""
		SELECT COUNT(f) > 0
		FROM Factura f
		WHERE f.contrato.id = :contratoId
		  AND f.periodo = :periodo
		  AND f.tipo = com.daos.entidades.TipoFactura.MENSUAL
		  AND f.estado <> com.daos.entidades.EstadoFactura.ANULADA
	""")
	boolean existeMensualNoAnulada(@Param("contratoId") Long contratoId, @Param("periodo") String periodo);

	@Query("""
		SELECT f
		FROM Factura f
		WHERE (:contratoId IS NULL OR f.contrato.id = :contratoId)
		  AND (:periodo IS NULL OR f.periodo = :periodo)
		  AND (:tipo IS NULL OR f.tipo = :tipo)
		ORDER BY f.id
	""")
	List<Factura> filtrar(@Param("contratoId") Long contratoId, @Param("periodo") String periodo,
			@Param("tipo") TipoFactura tipo);
}
