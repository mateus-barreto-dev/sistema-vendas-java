package com.sistemavendas.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import com.sistemavendas.domain.model.CPF;
import com.sistemavendas.domain.model.Cliente;

public class SessaoService {
    private final ClienteService clientes;
    private final Map<CPF, Cliente> sessoesAtivas;

    public SessaoService(int capacidadeMaxima, int limiteMaximoSessoes, ClienteService clientes){
        if (clientes == null) {
            throw new IllegalArgumentException("ClienteService Inválido");
        }
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade Inválida");
        }
        if (limiteMaximoSessoes <= 0) {
            throw new IllegalArgumentException("Limite de Sessões Inválido");
        }

        this.clientes = clientes;
        this.sessoesAtivas = new LinkedHashMap<>(capacidadeMaxima, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<CPF, Cliente> eldest) {
                return size() > limiteMaximoSessoes; // Se passar do limite, remove o mais antigo
                }
        };

    }


    public Optional<Cliente> autenticar(CPF cpf, String senha){
        Optional<Cliente> clienteOpt = clientes.autenticar(cpf, senha);
        clienteOpt.ifPresent(cliente -> sessoesAtivas.put(cpf, cliente));



        return clienteOpt;
    }
}
