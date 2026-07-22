package com.joyforliving.api.repository;

import com.joyforliving.api.domain.EstadoReserva;
import com.joyforliving.api.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Optional<Reserva> findByCodigo(String codigo);

    List<Reserva> findByEstado(EstadoReserva estado);

    List<Reserva> findByClienteIdOrderByFechaHoraDesc(Long clienteId);

    List<Reserva> findAllByOrderByFechaHoraDesc();

    /**
     * Reservas vigentes (no canceladas) de una actividad cuyo horario cae dentro
     * de la ventana consultada. Base del calculo de cupos disponibles.
     */
    @Query("""
           SELECT r FROM Reserva r
           WHERE r.actividad.id = :actividadId
             AND r.estado <> com.joyforliving.api.domain.EstadoReserva.CANCELADA
             AND r.fechaHora < :fin
             AND r.fechaHora >= :inicio
           """)
    List<Reserva> findVigentesEnVentana(@Param("actividadId") Long actividadId,
                                        @Param("inicio") LocalDateTime inicio,
                                        @Param("fin") LocalDateTime fin);

    @Query("""
           SELECT COALESCE(SUM(r.total), 0) FROM Reserva r
           WHERE r.estado IN (com.joyforliving.api.domain.EstadoReserva.CONFIRMADA,
                              com.joyforliving.api.domain.EstadoReserva.COMPLETADA)
           """)
    java.math.BigDecimal sumarIngresosConfirmados();

    long countByEstado(EstadoReserva estado);

    List<Reserva> findByFechaHoraBetweenOrderByFechaHoraAsc(LocalDateTime desde, LocalDateTime hasta);
}
