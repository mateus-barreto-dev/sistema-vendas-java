package com.sistemavendas.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sistemavendas.domain.strategy.DescontoStrategy;

public class Venda {
    private final CPF cliente;
    private final List<ItemVenda> itens = new ArrayList<>();
    private final BigDecimal percentualImposto;
    private final DescontoStrategy descontoStrategy;

    public Venda(CPF cliente, BigDecimal percentualImposto, DescontoStrategy descontoStrategy){
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente Inválido");
        }
        if(percentualImposto == null || percentualImposto.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Percentual Inválido");
        }
        if (descontoStrategy == null) {
            throw new IllegalArgumentException("Estratégia Inválida");
        }

        this.cliente = cliente;
        this.percentualImposto = percentualImposto;
        this.descontoStrategy = descontoStrategy;

    }

    public void adicionarItem(Produto produto, int quantidade){
        produto.darBaixaEstoque(quantidade);
        ItemVenda item = new ItemVenda(produto, quantidade);
        this.itens.add(item);
        
    }

    public BigDecimal calcularSubtotal(){
        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda itemVenda : itens) {
            total = total.add(itemVenda.getSubtotal());
        }

        return total;
    }

    public BigDecimal calcularImposto(){
        return this.percentualImposto.multiply(calcularSubtotal());
    }

    public BigDecimal calcularDesconto(){
        return descontoStrategy.calcularDesconto(calcularSubtotal());
    }

    public String gerarComprovante(){
        StringBuilder comprovante = new StringBuilder();

        comprovante.append("CPF: "+ cliente.getFormatado() + "\nProdutos:");

        for (ItemVenda itemVenda : itens) {
            Produto produto = itemVenda.getProduto();
            comprovante.append("\nProduto: " + produto.getNome() + " | Quantidade: " + itemVenda.getQuantidade() +
                                     " | Preço Unitário: " + produto.getPreco() + " | Total: " + itemVenda.getSubtotal());
        }

        comprovante.append("\n---------------------------------------------------------------------------------------");
        comprovante.append("\nTotal da Compra: " + this.calcularSubtotal().toString());
        comprovante.append("\nTotal de Impostos: " + this.calcularImposto().toString());
        comprovante.append("\nTotal de Descontos: " + this.calcularDesconto().toString());
        comprovante.append("\nTotal: " + this.calcularSubtotal().add(calcularImposto()).subtract(calcularDesconto()).toString());
        comprovante.append("\nObrigado Pela Compra! Volte Sempre!\n");

        return comprovante.toString();
    }

    public CPF getCliente(){
        return this.cliente;
    }

    public List<ItemVenda> getItens(){
        return List.copyOf(itens);
    }

    public DescontoStrategy getDescontoStrategy(){
        return this.descontoStrategy;
    }

    public BigDecimal getPercentualImposto(){
        return this.percentualImposto;
    }
}
