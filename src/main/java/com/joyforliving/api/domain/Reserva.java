package com.joyforliving.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Capacidad de negocio nuclear: "Gestion de reservas y disponibilidad". */
@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Codigo legible entregado al turista, p.ej. JFL-A7C2F1. */
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Integer numeroPersonas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @Column(length = 400)
    private String notas;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime creadaEn = LocalDateTime.now();

    /** Instante en que termina la experiencia; usado para detectar solapamientos. */
    @Transient
    public LocalDateTime getFechaHoraFin() {
        return fechaHora.plusMinutes(actividad.getDuracionMinutos());
    }
}
