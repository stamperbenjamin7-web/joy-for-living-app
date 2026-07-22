package com.joyforliving.api.repository;

import com.joyforliving.api.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query("""
           SELECT c FROM Cliente c
           WHERE LOWER(c.nombre)   LIKE LOWER(CONCAT('%', :texto, '%'))
              OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))
              OR LOWER(c.email)    LIKE LOWER(CONCAT('%', :texto, '%'))
           """)
    List<Cliente> buscar(@Param("texto") String texto);
}
