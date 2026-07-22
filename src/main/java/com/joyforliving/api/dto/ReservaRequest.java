package com.joyforliving.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReservaRequest(

        @NotNull(message = "Selecciona un cliente")
        Long clienteId,

        @NotNull(message = "Selecciona una experiencia")
        Long actividadId,

        @NotNull(message = "Indica la fecha y hora de la salida")
        @Future(message = "La salida debe programarse en una fecha futura")
        LocalDateTime fechaHora,

        @NotNull @Min(value = 1, message = "Reserva al menos un cupo")
        Integer numeroPersonas,

        @Size(max = 400)
        String notas
) {}
