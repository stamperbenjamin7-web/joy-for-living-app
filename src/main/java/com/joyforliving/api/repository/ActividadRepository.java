package com.joyforliving.api.repository;

import com.joyforliving.api.domain.Actividad;
import com.joyforliving.api.domain.CategoriaActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByActivaTrue();

    List<Actividad> findByCategoria(CategoriaActividad categoria);
}
