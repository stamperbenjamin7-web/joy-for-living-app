package com.joyforliving.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime instante,
        int estado,
        String error,
        String mensaje,
        Map<String, String> camposInvalidos
) {
    public static ApiError de(int estado, String error, String mensaje) {
        return new ApiError(LocalDateTime.now(), estado, error, mensaje, Map.of());
    }
}
