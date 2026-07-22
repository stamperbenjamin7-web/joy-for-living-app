package com.joyforliving.api.service;

import com.joyforliving.api.domain.Equipo;
import com.joyforliving.api.domain.EstadoEquipo;
import com.joyforliving.api.dto.EquipoRequest;
import com.joyforliving.api.dto.EquipoResponse;
import com.joyforliving.api.exception.RecursoNoEncontradoException;
import com.joyforliving.api.exception.ReglaNegocioException;
import com.joyforliving.api.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EquipoService {

    private final EquipoRepository repositorio;
    private final ActividadService actividadService;

    public EquipoService(EquipoRepository repositorio, ActividadService actividadService) {
        this.repositorio = repositorio;
        this.actividadService = actividadService;
    }

    public List<EquipoResponse> listar(EstadoEquipo estado) {
        List<Equipo> datos = (estado == null) ? repositorio.findAll() : repositorio.findByEstado(estado);
        return datos.stream().map(EquipoResponse::desde).toList();
    }

    public EquipoResponse obtener(Long id) {
        return EquipoResponse.desde(buscarEntidad(id));
    }

    private Equipo buscarEntidad(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("el equipo", id));
    }

    @Transactional
    public EquipoResponse crear(EquipoRequest datos) {
        validarInventario(datos);
        Equipo equipo = Equipo.builder()
                .nombre(datos.nombre())
                .tipo(datos.tipo())
                .cantidadTotal(datos.cantidadTotal())
                .cantidadDisponible(datos.cantidadDisponible())
                .estado(datos.estado() == null ? EstadoEquipo.DISPONIBLE : datos.estado())
                .ultimoMantenimiento(datos.ultimoMantenimiento())
                .actividad(datos.actividadId() == null ? null : actividadService.buscarEntidad(datos.actividadId()))
                .build();
        return EquipoResponse.desde(repositorio.save(equipo));
    }

    @Transactional
    public EquipoResponse actualizar(Long id, EquipoRequest datos) {
        validarInventario(datos);
        Equipo equipo = buscarEntidad(id);
        equipo.setNombre(datos.nombre());
        equipo.setTipo(datos.tipo());
        equipo.setCantidadTotal(datos.cantidadTotal());
        equipo.setCantidadDisponible(datos.cantidadDisponible());
        equipo.setUltimoMantenimiento(datos.ultimoMantenimiento());
        if (datos.estado() != null) {
            equipo.setEstado(datos.estado());
        }
        equipo.setActividad(datos.actividadId() == null ? null : actividadService.buscarEntidad(datos.actividadId()));
        return EquipoResponse.desde(repositorio.save(equipo));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscarEntidad(id));
    }

    private void validarInventario(EquipoRequest datos) {
        if (datos.cantidadDisponible() > datos.cantidadTotal()) {
            throw new ReglaNegocioException(
                    "Las unidades disponibles (" + datos.cantidadDisponible() + ") no pueden superar el total ("
                            + datos.cantidadTotal() + ").");
        }
    }
}
