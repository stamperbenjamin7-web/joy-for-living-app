package com.joyforliving.api.dto;

import java.time.LocalDateTime;

/** Respuesta del endpoint de disponibilidad de cupos. */
public record DisponibilidadResponse(
        Long actividadId,
        String actividadNombre,
        LocalDateTime fechaHora,
        Integer capacidadMaxima,
        Integer cuposOcupados,
        Integer cuposDisponibles,
        boolean hayCupo
) {}
