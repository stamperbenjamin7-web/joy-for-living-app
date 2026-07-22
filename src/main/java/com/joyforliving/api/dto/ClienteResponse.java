package com.joyforliving.api.dto;

import com.joyforliving.api.domain.Cliente;

import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellido,
        String nombreCompleto,
        String email,
        String telefono,
        String pais,
        LocalDate fechaRegistro
) {
    public static ClienteResponse desde(Cliente c) {
        return new ClienteResponse(
                c.getId(), c.getNombre(), c.getApellido(), c.getNombreCompleto(),
                c.getEmail(), c.getTelefono(), c.getPais(), c.getFechaRegistro());
    }
}
