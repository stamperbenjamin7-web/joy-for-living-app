package com.joyforliving.api.dto;

import com.joyforliving.api.domain.Equipo;
import com.joyforliving.api.domain.EstadoEquipo;

import java.time.LocalDate;

public record EquipoResponse(
        Long id,
        String nombre,
        String tipo,
        Integer cantidadTotal,
        Integer cantidadDisponible,
        EstadoEquipo estado,
        LocalDate ultimoMantenimiento,
        Long actividadId,
        String actividadNombre
) {
    public static EquipoResponse desde(Equipo e) {
        return new EquipoResponse(
                e.getId(), e.getNombre(), e.getTipo(), e.getCantidadTotal(),
                e.getCantidadDisponible(), e.getEstado(), e.getUltimoMantenimiento(),
                e.getActividad() != null ? e.getActividad().getId() : null,
                e.getActividad() != null ? e.getActividad().getNombre() : null);
    }
}
