package com.sistemavendas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.sistemavendas.domain.model.Venda;

public class RelatorioAnaliticoService {
    private List<Venda> vendas = new ArrayList<>();

    public void addVenda(Venda venda){
        if (venda == null) {
            throw new IllegalArgumentException("Venda Inválida");
        }

        vendas.add(venda);
    }

    public BigDecimal calcularReceitaTotal(){
        return vendas.stream().map(Venda::calcularTotalCompra).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularRendaMedia(){
        if (vendas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return calcularReceitaTotal().divide(BigDecimal.valueOf(vendas.size()),
                                                2, 
                                                java.math.RoundingMode.HALF_UP);
    }
}
