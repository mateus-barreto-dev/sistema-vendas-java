package com.sistemavendas.domain.exception;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String mensagem){
        super(mensagem);
    }
}
