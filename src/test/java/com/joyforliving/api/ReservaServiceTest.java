package com.joyforliving.api;

import com.joyforliving.api.domain.*;
import com.joyforliving.api.dto.DisponibilidadResponse;
import com.joyforliving.api.dto.ReservaRequest;
import com.joyforliving.api.dto.ReservaResponse;
import com.joyforliving.api.exception.ReglaNegocioException;
import com.joyforliving.api.repository.ReservaRepository;
import com.joyforliving.api.service.ActividadService;
import com.joyforliving.api.service.ClienteService;
import com.joyforliving.api.service.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Reglas de negocio de reservas")
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ClienteService clienteService;
    @Mock
    private ActividadService actividadService;

    private ReservaService servicio;

    private Actividad snorkel;
    private Cliente ana;
    private LocalDateTime salida;

    @BeforeEach
    void prepararEscenario() {
        servicio = new ReservaService(reservaRepository, clienteService, actividadService);

        snorkel = Actividad.builder()
                .id(1L)
                .nombre("Snorkel en Boca Catalina")
                .categoria(CategoriaActividad.SNORKEL)
                .duracionMinutos(120)
                .precioPorPersona(new BigDecimal("45.00"))
                .capacidadMaxima(12)
                .activa(true)
                .build();

        ana = Cliente.builder().id(10L).nombre("Ana").apellido("Rodriguez")
                .email("ana@example.com").build();

        salida = LocalDateTime.now().plusDays(3).withHour(9).withMinute(0);
    }

    @Test
    @DisplayName("Calcula el total multiplicando el precio por el numero de personas")
    void calculaElTotalDeLaReserva() {
        when(clienteService.buscarEntidad(10L)).thenReturn(ana);
        when(actividadService.buscarEntidad(1L)).thenReturn(snorkel);
        when(reservaRepository.findVigentesEnVentana(anyLong(), any(), any())).thenReturn(List.of());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        ReservaResponse creada = servicio.crear(
                new ReservaRequest(10L, 1L, salida, 3, "Familia con ninos"));

        assertThat(creada.total()).isEqualByComparingTo("135.00");
        assertThat(creada.estado()).isEqualTo(EstadoReserva.PENDIENTE);
        assertThat(creada.codigo()).startsWith("JFL-");
    }

    @Test
    @DisplayName("Rechaza la reserva cuando los cupos del horario ya estan comprometidos")
    void rechazaCuandoNoHayCupos() {
        when(clienteService.buscarEntidad(10L)).thenReturn(ana);
        when(actividadService.buscarEntidad(1L)).thenReturn(snorkel);
        when(reservaRepository.findVigentesEnVentana(anyLong(), any(), any()))
                .thenReturn(List.of(reservaDe(10), reservaDe(1)));

        assertThatThrownBy(() -> servicio.crear(new ReservaRequest(10L, 1L, salida, 3, null)))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Quedan 1 cupos");
    }

    @Test
    @DisplayName("Informa los cupos libres de una salida")
    void informaLaDisponibilidad() {
        when(actividadService.buscarEntidad(1L)).thenReturn(snorkel);
        when(reservaRepository.findVigentesEnVentana(anyLong(), any(), any()))
                .thenReturn(List.of(reservaDe(4), reservaDe(2)));

        DisponibilidadResponse disponibilidad = servicio.consultarDisponibilidad(1L, salida);

        assertThat(disponibilidad.cuposOcupados()).isEqualTo(6);
        assertThat(disponibilidad.cuposDisponibles()).isEqualTo(6);
        assertThat(disponibilidad.hayCupo()).isTrue();
    }

    @Test
    @DisplayName("No permite reactivar una reserva ya cancelada")
    void bloqueaTransicionesInvalidas() {
        Reserva cancelada = reservaDe(2);
        cancelada.setId(99L);
        cancelada.setEstado(EstadoReserva.CANCELADA);
        when(reservaRepository.findById(99L)).thenReturn(java.util.Optional.of(cancelada));

        assertThatThrownBy(() -> servicio.cambiarEstado(99L, EstadoReserva.CONFIRMADA))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("CANCELADA");
    }

    private Reserva reservaDe(int personas) {
        return Reserva.builder()
                .id((long) (personas * 100))
                .codigo("JFL-TEST" + personas)
                .cliente(ana)
                .actividad(snorkel)
                .fechaHora(salida)
                .numeroPersonas(personas)
                .total(new BigDecimal("45.00").multiply(BigDecimal.valueOf(personas)))
                .estado(EstadoReserva.CONFIRMADA)
                .creadaEn(LocalDateTime.now())
                .build();
    }
}
