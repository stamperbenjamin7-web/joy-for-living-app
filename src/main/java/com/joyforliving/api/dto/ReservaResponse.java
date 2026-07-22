package com.joyforliving.api.dto;

import com.joyforliving.api.domain.EstadoReserva;
import com.joyforliving.api.domain.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        String codigo,
        Long clienteId,
        String clienteNombre,
        String clienteEmail,
        Long actividadId,
        String actividadNombre,
        LocalDateTime fechaHora,
        Integer numeroPersonas,
        BigDecimal total,
        EstadoReserva estado,
        String notas,
        LocalDateTime creadaEn
) {
    public static ReservaResponse desde(Reserva r) {
        return new ReservaResponse(
                r.getId(), r.getCodigo(),
                r.getCliente().getId(), r.getCliente().getNombreCompleto(), r.getCliente().getEmail(),
                r.getActividad().getId(), r.getActividad().getNombre(),
                r.getFechaHora(), r.getNumeroPersonas(), r.getTotal(),
                r.getEstado(), r.getNotas(), r.getCreadaEn());
    }
}
