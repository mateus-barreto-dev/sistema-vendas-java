package com.sistemavendas.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        total = itens.stream().map(ItemVenda::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);


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

        comprovante.append("+-------------------------------------------------------------------------------------+\n");
        comprovante.append("CPF: "+ cliente.getFormatado() + "\nProdutos:");

        String produtos_comprados = itens.stream().map(ItemVenda::toString).collect(Collectors.joining("\n"));
        comprovante.append(produtos_comprados);

        comprovante.append("\n---------------------------------------------------------------------------------------");
        comprovante.append("\nTotal da Compra: " + this.calcularSubtotal().toString());
        comprovante.append("\nTotal de Impostos: " + this.calcularImposto().toString());
        comprovante.append("\nTotal de Descontos: " + this.calcularDesconto().toString());
        comprovante.append("\nTotal: " + calcularTotalCompra().toString());
        comprovante.append("\nObrigado Pela Compra! Volte Sempre!\n");
        comprovante.append("\n+-------------------------------------------------------------------------------------+");
        return comprovante.toString();
    }

    public BigDecimal calcularTotalCompra(){
        return this.calcularSubtotal().add(calcularImposto()).subtract(calcularDesconto());
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
