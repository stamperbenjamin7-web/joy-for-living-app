package com.joyforliving.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Capacidad de negocio: "Gestion de clientes y relaciones". */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(length = 60)
    private String pais;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate fechaRegistro = LocalDate.now();

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
