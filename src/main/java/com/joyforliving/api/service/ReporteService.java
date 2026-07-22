package com.joyforliving.api.service;

import com.joyforliving.api.domain.EstadoReserva;
import com.joyforliving.api.domain.Reserva;
import com.joyforliving.api.dto.ReservaResponse;
import com.joyforliving.api.dto.ResumenResponse;
import com.joyforliving.api.repository.ActividadRepository;
import com.joyforliving.api.repository.ClienteRepository;
import com.joyforliving.api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Capacidad de negocio: "Analitica y reportes operativos". */
@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final ReservaRepository reservaRepository;
    private final ActividadRepository actividadRepository;
    private final ClienteRepository clienteRepository;

    public ReporteService(ReservaRepository reservaRepository,
                          ActividadRepository actividadRepository,
                          ClienteRepository clienteRepository) {
        this.reservaRepository = reservaRepository;
        this.actividadRepository = actividadRepository;
        this.clienteRepository = clienteRepository;
    }

    public ResumenResponse resumen() {
        List<Reserva> todas = reservaRepository.findAll();

        Map<String, Long> porCategoria = todas.stream()
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .collect(Collectors.groupingBy(
                        r -> r.getActividad().getCategoria().name(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        LocalDateTime ahora = LocalDateTime.now();
        List<ReservaResponse> proximas = todas.stream()
                .filter(r -> r.getFechaHora().isAfter(ahora))
                .filter(r -> r.getEstado() != EstadoReserva.CANCELADA)
                .sorted((a, b) -> a.getFechaHora().compareTo(b.getFechaHora()))
                .limit(5)
                .map(ReservaResponse::desde)
                .toList();

        return new ResumenResponse(
                actividadRepository.count(),
                clienteRepository.count(),
                todas.size(),
                reservaRepository.countByEstado(EstadoReserva.PENDIENTE),
                reservaRepository.countByEstado(EstadoReserva.CONFIRMADA),
                reservaRepository.sumarIngresosConfirmados(),
                porCategoria,
                proximas);
    }
}
