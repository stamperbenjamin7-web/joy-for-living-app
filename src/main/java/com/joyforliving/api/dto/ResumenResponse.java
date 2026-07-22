package com.joyforliving.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** Indicadores del tablero operativo. */
public record ResumenResponse(
        long totalActividades,
        long totalClientes,
        long totalReservas,
        long reservasPendientes,
        long reservasConfirmadas,
        BigDecimal ingresosConfirmados,
        Map<String, Long> reservasPorCategoria,
        List<ReservaResponse> proximasSalidas
) {}
