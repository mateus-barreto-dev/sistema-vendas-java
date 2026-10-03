package com.sistemavendas.domain.model;

import java.math.BigDecimal;

public class ItemVenda {
    private final Produto produto;
    private final BigDecimal precoUnitario;
    private final int quantidade;

    public ItemVenda(Produto produto, int quantidade){
        if (produto == null) {
            throw new IllegalArgumentException("Produto Inválido");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade Inválida");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();

    }

    public BigDecimal getSubtotal(){
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public int getQuantidade(){
        return this.quantidade;
    }

    public Produto getProduto(){
        return this.produto;
    }

    public BigDecimal getPrecoUnitario(){
        return this.precoUnitario;
    }
}
