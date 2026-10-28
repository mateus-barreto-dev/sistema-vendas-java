package com.sistemavendas.domain.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.sistemavendas.domain.security.Acesso.Papel;

public class Cliente {
    private static final AtomicInteger contadorId = new AtomicInteger(0);
    private final int id;
    private String nome;
    private String email;
    private String senha;
    private Papel papel;
    private CPF cpf;

    public Cliente(String nome, String email, String senha, CPF cpf, Papel papel){
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome Inválido");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email Inválido");
        }
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha Inválida");
        }
        if (cpf == null) {
            throw new IllegalArgumentException("CPF Inválido");
        }
        if (papel == null) {
            throw new IllegalArgumentException("Papel Inválido");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Seu email não contém @ ou .");
        }

        this.id = contadorId.incrementAndGet();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.papel = papel;
    }

    @Override 
    public boolean equals(Object o){
        if (o == null) {
            return false;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }
        Cliente outro = (Cliente) o;
        return this.cpf.equals(outro.cpf);
    }

    @Override 
    public int hashCode(){
        return Objects.hash(this.cpf);
    }

    public CPF getCPF(){
        return this.cpf;
    }

    public String getSenha(){
        return this.senha;
    }

    public Papel getPapel(){
        return this.papel;
    }
}
