package com.joyforliving.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Capacidad de negocio: "Gestion de inventario y equipamiento". */
@Entity
@Table(name = "equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 60)
    private String tipo;

    @Column(nullable = false)
    private Integer cantidadTotal;

    @Column(nullable = false)
    private Integer cantidadDisponible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoEquipo estado = EstadoEquipo.DISPONIBLE;

    private LocalDate ultimoMantenimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;
}
