package com.daos.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity 

@Table (name = "HISTORIAL_ESTADO_INCIDENTE")

public class HistorialEstadoIncidente {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long Id;

    @ManyToOne (optional = false)
    @JoinColumn (name = "incidente:id", nullable = false)
    private Incidente incidente;

    @Enumerated (EnumType.STRING)
    private EstadoIncidente estadoAnterior;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private EstadoIncidente estadoNuevo;

    @Column (nullable = false)
    private LocalDateTime fechaALta;

    public HistorialEstadoIncidente(){
        
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public void setIncidente(Incidente incidente) {
        this.incidente = incidente;
    }

    public EstadoIncidente getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoIncidente estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoIncidente getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoIncidente estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaALta() {
        return fechaALta;
    }

    public void setFechaALta(LocalDateTime fechaALta) {
        this.fechaALta = fechaALta;
    }

}
