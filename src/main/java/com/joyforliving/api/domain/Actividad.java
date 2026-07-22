package com.joyforliving.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Experiencia comercializable del catalogo.
 * Capacidad de negocio: "Gestion de catalogo de experiencias".
 */
@Entity
@Table(name = "actividades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 600)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaActividad categoria;

    /** Duracion de la experiencia en minutos. */
    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioPorPersona;

    /** Cupos maximos por salida. */
    @Column(nullable = false)
    private Integer capacidadMaxima;

    @Column(length = 80)
    private String puntoEncuentro;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;
}
