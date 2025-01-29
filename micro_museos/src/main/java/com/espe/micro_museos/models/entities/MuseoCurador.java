package com.espe.micro_museos.models.entities;

import com.espe.micro_museos.models.Curador;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "museo_curador")
public class MuseoCurador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "museo_id", nullable = false)
    @JsonBackReference // Evita la serialización infinita
    private Museo museo;

    @NotNull(message = "El ID del curador no puede ser nulo.")
    @Column(name = "curador_id", nullable = false)
    private Long curadorId;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @PrePersist
    protected void onAssign() {
        this.fechaAsignacion = LocalDateTime.now();
    }

    public MuseoCurador() {}

    public MuseoCurador(Museo museo, Long curadorId) {
        this.museo = museo;
        this.curadorId = curadorId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Museo getMuseo() {
        return museo;
    }

    public void setMuseo(Museo museo) {
        this.museo = museo;
    }

    public Long getCuradorId() {
        return curadorId;
    }

    public void setCuradorId(Long curadorId) {
        this.curadorId = curadorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
}
