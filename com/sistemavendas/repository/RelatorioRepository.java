package com.sistemavendas.repository;

import java.util.List;

public interface RelatorioRepository {

    void salvar(String conteudoComprovante);
    List<String> lerTodos();
}
