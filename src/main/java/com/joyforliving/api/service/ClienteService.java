package com.joyforliving.api.service;

import com.joyforliving.api.domain.Cliente;
import com.joyforliving.api.dto.ClienteRequest;
import com.joyforliving.api.dto.ClienteResponse;
import com.joyforliving.api.exception.RecursoNoEncontradoException;
import com.joyforliving.api.exception.ReglaNegocioException;
import com.joyforliving.api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ClienteService {

    private final ClienteRepository repositorio;

    public ClienteService(ClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ClienteResponse> listar(String texto) {
        List<Cliente> datos = (texto == null || texto.isBlank())
                ? repositorio.findAll()
                : repositorio.buscar(texto.trim());
        return datos.stream().map(ClienteResponse::desde).toList();
    }

    public ClienteResponse obtener(Long id) {
        return ClienteResponse.desde(buscarEntidad(id));
    }

    public Cliente buscarEntidad(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("el cliente", id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest datos) {
        if (repositorio.existsByEmailIgnoreCase(datos.email())) {
            throw new ReglaNegocioException("Ya existe un cliente registrado con el correo " + datos.email() + ".");
        }
        Cliente cliente = Cliente.builder()
                .nombre(datos.nombre())
                .apellido(datos.apellido())
                .email(datos.email().toLowerCase())
                .telefono(datos.telefono())
                .pais(datos.pais())
                .fechaRegistro(LocalDate.now())
                .build();
        return ClienteResponse.desde(repositorio.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest datos) {
        Cliente cliente = buscarEntidad(id);
        repositorio.findByEmailIgnoreCase(datos.email())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ReglaNegocioException("Otro cliente ya usa el correo " + datos.email() + ".");
                });
        cliente.setNombre(datos.nombre());
        cliente.setApellido(datos.apellido());
        cliente.setEmail(datos.email().toLowerCase());
        cliente.setTelefono(datos.telefono());
        cliente.setPais(datos.pais());
        return ClienteResponse.desde(repositorio.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        repositorio.delete(buscarEntidad(id));
    }
}
