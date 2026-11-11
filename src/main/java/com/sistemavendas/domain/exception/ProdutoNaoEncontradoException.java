package com.sistemavendas.domain.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
