package com.joyforliving.api.exception;

/** Se lanza cuando la operacion es valida en forma pero rompe una regla del negocio. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
