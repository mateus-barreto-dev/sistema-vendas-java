package com.sistemavendas.repository;

import java.util.List;
import java.util.function.Predicate;


public interface RelatorioRepository {

    void salvar(String conteudoComprovante);
    List<String> lerTodos();
    List<String> buscarNoHistorico(Predicate<String> criterio);
}
