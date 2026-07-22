package com.joyforliving.api.service;

import com.joyforliving.api.domain.Actividad;
import com.joyforliving.api.domain.Cliente;
import com.joyforliving.api.domain.EstadoReserva;
import com.joyforliving.api.domain.Reserva;
import com.joyforliving.api.dto.DisponibilidadResponse;
import com.joyforliving.api.dto.ReservaRequest;
import com.joyforliving.api.dto.ReservaResponse;
import com.joyforliving.api.exception.RecursoNoEncontradoException;
import com.joyforliving.api.exception.ReglaNegocioException;
import com.joyforliving.api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Nucleo del dominio: crea reservas validando cupos, calcula importes y
 * gobierna las transiciones de estado.
 */
@Service
@Transactional(readOnly = true)
public class ReservaService {

    private final ReservaRepository repositorio;
    private final ClienteService clienteService;
    private final ActividadService actividadService;

    public ReservaService(ReservaRepository repositorio,
                          ClienteService clienteService,
                          ActividadService actividadService) {
        this.repositorio = repositorio;
        this.clienteService = clienteService;
        this.actividadService = actividadService;
    }

    public List<ReservaResponse> listar(EstadoReserva estado, Long clienteId) {
        List<Reserva> datos;
        if (estado != null) {
            datos = repositorio.findByEstado(estado);
        } else if (clienteId != null) {
            datos = repositorio.findByClienteIdOrderByFechaHoraDesc(clienteId);
        } else {
            datos = repositorio.findAllByOrderByFechaHoraDesc();
        }
        return datos.stream().map(ReservaResponse::desde).toList();
    }

    public ReservaResponse obtener(Long id) {
        return ReservaResponse.desde(buscarEntidad(id));
    }

    public ReservaResponse obtenerPorCodigo(String codigo) {
        return repositorio.findByCodigo(codigo)
                .map(ReservaResponse::desde)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una reserva con el codigo " + codigo + "."));
    }

    private Reserva buscarEntidad(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("la reserva", id));
    }

    /** Cupos ya comprometidos para una salida, sin contar reservas canceladas. */
    public DisponibilidadResponse consultarDisponibilidad(Long actividadId, LocalDateTime fechaHora) {
        Actividad actividad = actividadService.buscarEntidad(actividadId);
        int ocupados = cuposOcupados(actividad, fechaHora, null);
        int disponibles = Math.max(0, actividad.getCapacidadMaxima() - ocupados);
        return new DisponibilidadResponse(
                actividad.getId(), actividad.getNombre(), fechaHora,
                actividad.getCapacidadMaxima(), ocupados, disponibles, disponibles > 0);
    }

    @Transactional
    public ReservaResponse crear(ReservaRequest datos) {
        Cliente cliente = clienteService.buscarEntidad(datos.clienteId());
        Actividad actividad = actividadService.buscarEntidad(datos.actividadId());

        if (Boolean.FALSE.equals(actividad.getActiva())) {
            throw new ReglaNegocioException(
                    "La experiencia " + actividad.getNombre() + " no esta disponible para la venta.");
        }
        if (datos.numeroPersonas() > actividad.getCapacidadMaxima()) {
            throw new ReglaNegocioException("La experiencia admite hasta "
                    + actividad.getCapacidadMaxima() + " personas por salida.");
        }

        int ocupados = cuposOcupados(actividad, datos.fechaHora(), null);
        int disponibles = actividad.getCapacidadMaxima() - ocupados;
        if (datos.numeroPersonas() > disponibles) {
            throw new ReglaNegocioException("Quedan " + Math.max(0, disponibles)
                    + " cupos para ese horario. Elige otra hora o reduce el numero de personas.");
        }

        BigDecimal total = actividad.getPrecioPorPersona()
                .multiply(BigDecimal.valueOf(datos.numeroPersonas()));

        Reserva reserva = Reserva.builder()
                .codigo(generarCodigo())
                .cliente(cliente)
                .actividad(actividad)
                .fechaHora(datos.fechaHora())
                .numeroPersonas(datos.numeroPersonas())
                .total(total)
                .estado(EstadoReserva.PENDIENTE)
                .notas(datos.notas())
                .creadaEn(LocalDateTime.now())
                .build();

        return ReservaResponse.desde(repositorio.save(reserva));
    }

    @Transactional
    public ReservaResponse cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = buscarEntidad(id);
        validarTransicion(reserva.getEstado(), nuevoEstado);
        reserva.setEstado(nuevoEstado);
        return ReservaResponse.desde(repositorio.save(reserva));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscarEntidad(id));
    }

    // ------------------------------------------------------------------
    // Reglas internas
    // ------------------------------------------------------------------

    /**
     * Suma los cupos de las reservas vigentes que se solapan con la salida
     * consultada. Dos salidas de la misma experiencia se solapan cuando la
     * diferencia entre sus horas de inicio es menor que la duracion.
     */
    private int cuposOcupados(Actividad actividad, LocalDateTime fechaHora, Long reservaExcluidaId) {
        int duracion = actividad.getDuracionMinutos();
        LocalDateTime inicio = fechaHora.minusMinutes(duracion - 1L);
        LocalDateTime fin = fechaHora.plusMinutes(duracion);

        return repositorio.findVigentesEnVentana(actividad.getId(), inicio, fin).stream()
                .filter(r -> reservaExcluidaId == null || !r.getId().equals(reservaExcluidaId))
                .mapToInt(Reserva::getNumeroPersonas)
                .sum();
    }

    private void validarTransicion(EstadoReserva actual, EstadoReserva nuevo) {
        if (actual == nuevo) {
            return;
        }
        boolean permitida = switch (actual) {
            case PENDIENTE -> nuevo == EstadoReserva.CONFIRMADA || nuevo == EstadoReserva.CANCELADA;
            case CONFIRMADA -> nuevo == EstadoReserva.COMPLETADA || nuevo == EstadoReserva.CANCELADA;
            case CANCELADA, COMPLETADA -> false;
        };
        if (!permitida) {
            throw new ReglaNegocioException(
                    "Una reserva " + actual + " no puede pasar a " + nuevo + ".");
        }
    }

    private String generarCodigo() {
        String sufijo = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "JFL-" + sufijo;
    }
}
