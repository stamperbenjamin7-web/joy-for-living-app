package com.joyforliving.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "El nombre es obligatorio") @Size(max = 80)
        String nombre,

        @NotBlank(message = "El apellido es obligatorio") @Size(max = 80)
        String apellido,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Escribe un correo valido")
        @Size(max = 120)
        String email,

        @Size(max = 30)
        String telefono,

        @Size(max = 60)
        String pais
) {}
