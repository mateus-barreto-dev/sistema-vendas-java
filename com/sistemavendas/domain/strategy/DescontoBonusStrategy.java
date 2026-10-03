package com.sistemavendas.domain.strategy;

import java.math.BigDecimal;

public class DescontoBonusStrategy implements DescontoStrategy {
    private final int pontos;

    public DescontoBonusStrategy(int pontos){
        if (pontos < 0) {
            throw new IllegalArgumentException("Quantidade de Pontos não pode ser negativo");
        }

        this.pontos = pontos;
    }

    @Override
    public BigDecimal calcularDesconto(BigDecimal valorTotal) {
        if (this.pontos >= 10) {
            return valorTotal.multiply(new BigDecimal("0.05"));
        }

        return BigDecimal.ZERO;
    }
    
}
