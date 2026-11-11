package com.sistemavendas.domain.exception;

public class ClienteJaCadastradoException extends RuntimeException{
    public ClienteJaCadastradoException(String mensagem){
        super(mensagem);
    }
}
