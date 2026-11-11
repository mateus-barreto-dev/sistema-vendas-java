package com.sistemavendas.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.sistemavendas.domain.exception.EstoqueInsuficienteException;

public class Produto {
    private final int id;
    private String nome;
    private BigDecimal preco;
    private int quantidadeEstoque; 
    private int quantidadeVendida;
    
    public Produto(int id, String nome, BigDecimal preco, int quantidadeEstoque){
        if(nome == null || nome.isBlank()){
            throw new IllegalArgumentException("Nome Inválido");
        }
        if(preco == null || preco.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Preço Inválido");
        }
        if(quantidadeEstoque < 0){
            throw new IllegalArgumentException("Valor de Estoque Inválido");
        }

        this.nome = nome;
        this.id = id;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.quantidadeVendida = 0;
       
    }

    public void darBaixaEstoque(int quantidade){
        if(quantidade <= 0){
            throw new IllegalArgumentException("Quantidade Inválida: A Quantidade deve ser maior que 0");
        }
        if(quantidade > this.quantidadeEstoque){
            throw new EstoqueInsuficienteException("Quantidade solicitada indisponível no estoque.");
        }
        this.quantidadeEstoque = this.quantidadeEstoque - quantidade;
        this.quantidadeVendida = this.quantidadeVendida + quantidade;

    }

    public void adicionarEstoque(int quantidade){
        if(quantidade <= 0){
            throw new IllegalArgumentException("Quantidade Inválida: A Quantidade deve ser maior que 0");
        }

        this.quantidadeEstoque = this.quantidadeEstoque + quantidade;
    }

    public void atualizarPreco(BigDecimal precoNovo){
        if(precoNovo == null || precoNovo.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Preço Inválido: O preço deve ser maior que R$ 0,00");
        }

        this.preco = precoNovo;
    }

    public void atualizarNome(String nomeNovo){
        if (nomeNovo == null || nomeNovo.isBlank()) {
            throw new IllegalArgumentException("Nome Inválido: O nome não pode ser nulo");
        }
        this.nome = nomeNovo;
    }

    public void atualizarQuantidade(int quantidade){
        if(quantidade < 0){
            throw new IllegalArgumentException("Quantidade Inválida: A Quantidade deve ser Positiva");
        }
        this.quantidadeEstoque = quantidade;
    }


    public String getNome(){
        return this.nome;
    }

    public int getId(){
        return this.id;
    }

    public int getQuantidadeVendida(){
        return this.quantidadeVendida;
    }

    public int getQuantidadeEstoque(){
        return this.quantidadeEstoque;
    }

    public BigDecimal getPreco(){
        return this.preco;
    }

   
    @Override 
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (!(o instanceof Produto)) {
            return false;
        }

        Produto produto = (Produto)o;

        return this.id == produto.getId();
    }

    @Override 
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Id: %d | Nome: %s | Preço: R$ %.2f | Estoque: %d", 
                id, nome, preco, quantidadeEstoque);
    }
}
