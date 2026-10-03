package com.sistemavendas.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

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
            throw new IllegalStateException("Quantidade Inválida: A Quantidade excede a Quantidade Estocada");
        }
        this.quantidadeEstoque = this.quantidadeEstoque - quantidade;
        this.quantidadeVendida = this.quantidadeVendida + quantidade;

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
