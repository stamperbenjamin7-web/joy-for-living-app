package com.joyforliving.api.exception;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Object id) {
        super("No encontramos " + recurso + " con identificador " + id + ".");
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
