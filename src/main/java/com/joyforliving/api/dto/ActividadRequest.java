package com.joyforliving.api.dto;

import com.joyforliving.api.domain.CategoriaActividad;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ActividadRequest(

        @NotBlank(message = "El nombre de la experiencia es obligatorio")
        @Size(max = 120)
        String nombre,

        @Size(max = 600)
        String descripcion,

        @NotNull(message = "Selecciona una categoria")
        CategoriaActividad categoria,

        @NotNull @Min(value = 15, message = "La duracion minima es de 15 minutos")
        Integer duracionMinutos,

        @NotNull @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a cero")
        BigDecimal precioPorPersona,

        @NotNull @Min(value = 1, message = "La capacidad minima es 1 persona")
        Integer capacidadMaxima,

        @Size(max = 80)
        String puntoEncuentro,

        Boolean activa
) {}
