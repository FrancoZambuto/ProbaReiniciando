package com.daos.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "INCIDENTE")
public class Incidente {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @ManyToOne (optional = false)
    @JoinColumn (name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column (nullable = false)
    private String titulo;

    @Column (nullable = false, length = 1000)
    private String descripcion;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private CategoriaIncidente categoria;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private PrioridadIncidente prioridad;
    
    @Column (nullable = false)
    private LocalDateTime fechaALta;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private EstadoIncidente estado;

    public Incidente(){
        
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CategoriaIncidente getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaIncidente categoria) {
        this.categoria = categoria;
    }

    public PrioridadIncidente getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadIncidente prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDateTime getFechaALta() {
        return fechaALta;
    }

    public void setFechaALta(LocalDateTime fechaALta) {
        this.fechaALta = fechaALta;
    }

    public EstadoIncidente getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidente estado) {
        this.estado = estado;
    }

}
