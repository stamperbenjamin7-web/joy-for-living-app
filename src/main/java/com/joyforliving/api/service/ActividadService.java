package com.joyforliving.api.service;

import com.joyforliving.api.domain.Actividad;
import com.joyforliving.api.domain.CategoriaActividad;
import com.joyforliving.api.dto.ActividadRequest;
import com.joyforliving.api.dto.ActividadResponse;
import com.joyforliving.api.exception.RecursoNoEncontradoException;
import com.joyforliving.api.repository.ActividadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActividadService {

    private final ActividadRepository repositorio;

    public ActividadService(ActividadRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ActividadResponse> listar(CategoriaActividad categoria, Boolean soloActivas) {
        List<Actividad> datos;
        if (categoria != null) {
            datos = repositorio.findByCategoria(categoria);
        } else if (Boolean.TRUE.equals(soloActivas)) {
            datos = repositorio.findByActivaTrue();
        } else {
            datos = repositorio.findAll();
        }
        return datos.stream().map(ActividadResponse::desde).toList();
    }

    public ActividadResponse obtener(Long id) {
        return ActividadResponse.desde(buscarEntidad(id));
    }

    public Actividad buscarEntidad(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("la experiencia", id));
    }

    @Transactional
    public ActividadResponse crear(ActividadRequest datos) {
        Actividad actividad = Actividad.builder()
                .nombre(datos.nombre())
                .descripcion(datos.descripcion())
                .categoria(datos.categoria())
                .duracionMinutos(datos.duracionMinutos())
                .precioPorPersona(datos.precioPorPersona())
                .capacidadMaxima(datos.capacidadMaxima())
                .puntoEncuentro(datos.puntoEncuentro())
                .activa(datos.activa() == null || datos.activa())
                .build();
        return ActividadResponse.desde(repositorio.save(actividad));
    }

    @Transactional
    public ActividadResponse actualizar(Long id, ActividadRequest datos) {
        Actividad actividad = buscarEntidad(id);
        actividad.setNombre(datos.nombre());
        actividad.setDescripcion(datos.descripcion());
        actividad.setCategoria(datos.categoria());
        actividad.setDuracionMinutos(datos.duracionMinutos());
        actividad.setPrecioPorPersona(datos.precioPorPersona());
        actividad.setCapacidadMaxima(datos.capacidadMaxima());
        actividad.setPuntoEncuentro(datos.puntoEncuentro());
        if (datos.activa() != null) {
            actividad.setActiva(datos.activa());
        }
        return ActividadResponse.desde(repositorio.save(actividad));
    }

    @Transactional
    public void eliminar(Long id) {
        Actividad actividad = buscarEntidad(id);
        // Baja logica: conserva el historico de reservas asociado.
        actividad.setActiva(false);
        repositorio.save(actividad);
    }
}
