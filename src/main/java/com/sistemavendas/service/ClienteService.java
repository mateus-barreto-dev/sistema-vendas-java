package com.sistemavendas.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sistemavendas.domain.exception.ClienteJaCadastradoException;
import com.sistemavendas.domain.model.CPF;
import com.sistemavendas.domain.model.Cliente;

public class ClienteService {
    private final Map<CPF, Cliente> clientes = new HashMap<>();

    public void cadastrarCliente(Cliente cliente){
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente Inválido");
        }

        CPF cpfCliente = cliente.getCPF();

        if (clientes.containsKey(cpfCliente)) {
            throw new ClienteJaCadastradoException("Este CPF já foi cadastrado");
        }

        clientes.put(cpfCliente, cliente);
    }

    public Optional<Cliente> encontrarCliente(CPF cpf){
        return Optional.ofNullable(clientes.get(cpf));
    }

    public  Optional<Cliente> autenticar(CPF cpf, String senha){
        return encontrarCliente(cpf).filter(cliente -> cliente.getSenha().equals(senha));
    };
   












}
