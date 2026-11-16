package com.daos.accesoDatos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daos.entidades.Pago;

@Repository
public interface IPagoRepo extends JpaRepository<Pago, Long> {

    boolean existsByFacturaId(Long facturaId);

    List<Pago> findByFacturaId(Long facturaId);
}