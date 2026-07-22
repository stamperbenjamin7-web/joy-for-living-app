package com.joyforliving.api.repository;

import com.joyforliving.api.domain.Equipo;
import com.joyforliving.api.domain.EstadoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByEstado(EstadoEquipo estado);

    List<Equipo> findByActividadId(Long actividadId);
}
