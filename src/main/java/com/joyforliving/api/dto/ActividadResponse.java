package com.joyforliving.api.dto;

import com.joyforliving.api.domain.Actividad;
import com.joyforliving.api.domain.CategoriaActividad;

import java.math.BigDecimal;

public record ActividadResponse(
        Long id,
        String nombre,
        String descripcion,
        CategoriaActividad categoria,
        Integer duracionMinutos,
        BigDecimal precioPorPersona,
        Integer capacidadMaxima,
        String puntoEncuentro,
        Boolean activa
) {
    public static ActividadResponse desde(Actividad a) {
        return new ActividadResponse(
                a.getId(), a.getNombre(), a.getDescripcion(), a.getCategoria(),
                a.getDuracionMinutos(), a.getPrecioPorPersona(), a.getCapacidadMaxima(),
                a.getPuntoEncuentro(), a.getActiva());
    }
}
