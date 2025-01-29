package com.espe.micro_curadores.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "curadores")
public class Curador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras y espacios")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "La especialidad no puede estar vacía")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "La especialidad solo puede contener letras y espacios")
    @Column(nullable = false)
    private String especialidad;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "creado_en", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creadoEn;

    @PrePersist
    public void prePersist() {
        this.creadoEn = new Date();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento != null) {
            LocalDate fechaInicio = LocalDate.of(1930, 1, 1);
            LocalDate fechaActual = LocalDate.now();

            if (fechaNacimiento.isBefore(fechaInicio) || fechaNacimiento.isAfter(fechaActual)) {
                throw new IllegalArgumentException("La fecha de nacimiento debe estar entre 1930 y el año actual");
            }
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }
}
