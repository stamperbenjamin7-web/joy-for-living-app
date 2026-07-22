package com.joyforliving.api.controller;

import com.joyforliving.api.domain.CategoriaActividad;
import com.joyforliving.api.dto.ActividadRequest;
import com.joyforliving.api.dto.ActividadResponse;
import com.joyforliving.api.service.ActividadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@Tag(name = "Experiencias", description = "Catalogo de actividades acuaticas")
public class ActividadController {

    private final ActividadService servicio;

    public ActividadController(ActividadService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @Operation(summary = "Lista las experiencias del catalogo")
    public List<ActividadResponse> listar(@RequestParam(required = false) CategoriaActividad categoria,
                                          @RequestParam(required = false) Boolean soloActivas) {
        return servicio.listar(categoria, soloActivas);
    }

    @GetMapping("/{id}")
    public ActividadResponse obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActividadResponse crear(@Valid @RequestBody ActividadRequest datos) {
        return servicio.crear(datos);
    }

    @PutMapping("/{id}")
    public ActividadResponse actualizar(@PathVariable Long id, @Valid @RequestBody ActividadRequest datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Retira la experiencia del catalogo (baja logica)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
