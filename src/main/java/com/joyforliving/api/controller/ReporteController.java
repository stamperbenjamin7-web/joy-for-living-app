package com.joyforliving.api.controller;

import com.joyforliving.api.dto.ResumenResponse;
import com.joyforliving.api.service.ReporteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService servicio;

    public ReporteController(ReporteService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/resumen")
    public ResumenResponse resumen() {
        return servicio.resumen();
    }
}
