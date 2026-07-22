package com.joyforliving.api.config;

import com.joyforliving.api.domain.*;
import com.joyforliving.api.repository.ActividadRepository;
import com.joyforliving.api.repository.ClienteRepository;
import com.joyforliving.api.repository.EquipoRepository;
import com.joyforliving.api.repository.ReservaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Datos de demostracion para el perfil de desarrollo. Permite abrir la
 * aplicacion y ver el tablero con informacion realista sin cargar nada a mano.
 */
@Configuration
@Profile("dev")
public class CargaDatosIniciales {

    @Bean
    CommandLineRunner sembrarDatos(ActividadRepository actividades,
                                   ClienteRepository clientes,
                                   EquipoRepository equipos,
                                   ReservaRepository reservas) {
        return args -> {
            if (actividades.count() > 0) {
                return;
            }

            Actividad snorkel = actividades.save(Actividad.builder()
                    .nombre("Snorkel en Boca Catalina")
                    .descripcion("Salida guiada al arrecife de Boca Catalina con equipo incluido y refrigerio a bordo.")
                    .categoria(CategoriaActividad.SNORKEL)
                    .duracionMinutos(120)
                    .precioPorPersona(new BigDecimal("45.00"))
                    .capacidadMaxima(12)
                    .puntoEncuentro("Palm Beach Pier")
                    .activa(true)
                    .build());

            Actividad kayak = actividades.save(Actividad.builder()
                    .nombre("Kayak transparente en Mangel Halto")
                    .descripcion("Recorrido en kayak de fondo transparente por los manglares del sur de la isla.")
                    .categoria(CategoriaActividad.KAYAK)
                    .duracionMinutos(90)
                    .precioPorPersona(new BigDecimal("38.50"))
                    .capacidadMaxima(8)
                    .puntoEncuentro("Mangel Halto Beach")
                    .activa(true)
                    .build());

            Actividad catamaran = actividades.save(Actividad.builder()
                    .nombre("Catamaran al atardecer")
                    .descripcion("Navegacion de dos horas por la costa oeste con barra abierta y musica en vivo.")
                    .categoria(CategoriaActividad.CATAMARAN)
                    .duracionMinutos(150)
                    .precioPorPersona(new BigDecimal("79.00"))
                    .capacidadMaxima(30)
                    .puntoEncuentro("Marina Renaissance")
                    .activa(true)
                    .build());

            Actividad jetSki = actividades.save(Actividad.builder()
                    .nombre("Jet ski por Palm Beach")
                    .descripcion("Circuito de 45 minutos en moto acuatica con instructor certificado.")
                    .categoria(CategoriaActividad.JET_SKI)
                    .duracionMinutos(45)
                    .precioPorPersona(new BigDecimal("95.00"))
                    .capacidadMaxima(6)
                    .puntoEncuentro("Palm Beach Pier")
                    .activa(true)
                    .build());

            Actividad paddle = actividades.save(Actividad.builder()
                    .nombre("Paddle board al amanecer")
                    .descripcion("Sesion matutina de paddle board en aguas planas, apta para principiantes.")
                    .categoria(CategoriaActividad.PADDLE_BOARD)
                    .duracionMinutos(60)
                    .precioPorPersona(new BigDecimal("30.00"))
                    .capacidadMaxima(10)
                    .puntoEncuentro("Eagle Beach")
                    .activa(true)
                    .build());

            equipos.save(Equipo.builder().nombre("Set de snorkel adulto").tipo("Snorkel")
                    .cantidadTotal(24).cantidadDisponible(21).estado(EstadoEquipo.DISPONIBLE)
                    .ultimoMantenimiento(LocalDate.now().minusDays(12)).actividad(snorkel).build());
            equipos.save(Equipo.builder().nombre("Kayak transparente biplaza").tipo("Embarcacion")
                    .cantidadTotal(8).cantidadDisponible(6).estado(EstadoEquipo.DISPONIBLE)
                    .ultimoMantenimiento(LocalDate.now().minusDays(30)).actividad(kayak).build());
            equipos.save(Equipo.builder().nombre("Moto acuatica Yamaha VX").tipo("Motorizado")
                    .cantidadTotal(3).cantidadDisponible(2).estado(EstadoEquipo.MANTENIMIENTO)
                    .ultimoMantenimiento(LocalDate.now().minusDays(3)).actividad(jetSki).build());
            equipos.save(Equipo.builder().nombre("Tabla de paddle rigida").tipo("Tabla")
                    .cantidadTotal(12).cantidadDisponible(12).estado(EstadoEquipo.DISPONIBLE)
                    .ultimoMantenimiento(LocalDate.now().minusDays(20)).actividad(paddle).build());
            equipos.save(Equipo.builder().nombre("Chaleco salvavidas certificado").tipo("Seguridad")
                    .cantidadTotal(40).cantidadDisponible(35).estado(EstadoEquipo.DISPONIBLE)
                    .ultimoMantenimiento(LocalDate.now().minusDays(8)).build());

            Cliente ana = clientes.save(Cliente.builder().nombre("Ana").apellido("Rodriguez")
                    .email("ana.rodriguez@example.com").telefono("+297 560 1122").pais("Colombia").build());
            Cliente mark = clientes.save(Cliente.builder().nombre("Mark").apellido("de Vries")
                    .email("mark.devries@example.com").telefono("+31 6 2211 8890").pais("Paises Bajos").build());
            Cliente laura = clientes.save(Cliente.builder().nombre("Laura").apellido("Kingsley")
                    .email("laura.kingsley@example.com").telefono("+1 305 771 4402").pais("Estados Unidos").build());
            clientes.save(Cliente.builder().nombre("Diego").apellido("Salazar")
                    .email("diego.salazar@example.com").telefono("+593 99 812 3344").pais("Ecuador").build());

            LocalDateTime manana = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0));

            reservas.save(Reserva.builder().codigo("JFL-1A2B3C").cliente(ana).actividad(snorkel)
                    .fechaHora(manana).numeroPersonas(2).total(new BigDecimal("90.00"))
                    .estado(EstadoReserva.CONFIRMADA).notas("Pareja sin experiencia previa.")
                    .creadaEn(LocalDateTime.now().minusDays(2)).build());

            reservas.save(Reserva.builder().codigo("JFL-4D5E6F").cliente(mark).actividad(catamaran)
                    .fechaHora(manana.plusDays(1).withHour(17)).numeroPersonas(4)
                    .total(new BigDecimal("316.00")).estado(EstadoReserva.PENDIENTE)
                    .notas("Solicita menu vegetariano.")
                    .creadaEn(LocalDateTime.now().minusDays(1)).build());

            reservas.save(Reserva.builder().codigo("JFL-7G8H9I").cliente(laura).actividad(kayak)
                    .fechaHora(manana.plusDays(2).withHour(10)).numeroPersonas(3)
                    .total(new BigDecimal("115.50")).estado(EstadoReserva.CONFIRMADA)
                    .creadaEn(LocalDateTime.now().minusHours(6)).build());
        };
    }
}
