package com.daos.accesoDatos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daos.entidades.Contrato;

@Repository
public interface IContratoRepo extends JpaRepository<Contrato, Long> {

}
