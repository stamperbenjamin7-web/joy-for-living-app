package com.joyforliving.api.controller;

import com.joyforliving.api.domain.EstadoReserva;
import com.joyforliving.api.dto.DisponibilidadResponse;
import com.joyforliving.api.dto.ReservaRequest;
import com.joyforliving.api.dto.ReservaResponse;
import com.joyforliving.api.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService servicio;

    public ReservaController(ReservaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<ReservaResponse> listar(@RequestParam(required = false) EstadoReserva estado,
                                        @RequestParam(required = false) Long clienteId) {
        return servicio.listar(estado, clienteId);
    }

    @GetMapping("/{id}")
    public ReservaResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Consulta publica de una reserva por su codigo")
    public ReservaResponse porCodigo(@PathVariable String codigo) {
        return servicio.obtenerPorCodigo(codigo);
    }

    @GetMapping("/disponibilidad")
    @Operation(summary = "Cupos libres de una experiencia en una fecha y hora")
    public DisponibilidadResponse disponibilidad(
            @RequestParam Long actividadId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora) {
        return servicio.consultarDisponibilidad(actividadId, fechaHora);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponse crear(@Valid @RequestBody ReservaRequest datos) {
        return servicio.crear(datos);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Confirma, cancela o completa una reserva")
    public ReservaResponse cambiarEstado(@PathVariable Long id, @RequestParam EstadoReserva valor) {
        return servicio.cambiarEstado(id, valor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
