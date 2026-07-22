package com.joyforliving.api.dto;

import com.joyforliving.api.domain.EstadoEquipo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EquipoRequest(

        @NotBlank(message = "El nombre del equipo es obligatorio") @Size(max = 120)
        String nombre,

        @NotBlank(message = "Indica el tipo de equipo") @Size(max = 60)
        String tipo,

        @NotNull @Min(value = 1, message = "Registra al menos una unidad")
        Integer cantidadTotal,

        @NotNull @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
        Integer cantidadDisponible,

        EstadoEquipo estado,

        LocalDate ultimoMantenimiento,

        Long actividadId
) {}
