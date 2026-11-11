package com.sistemavendas.domain.strategy;

import java.math.BigDecimal;

public class SemDescontoStrategy implements DescontoStrategy {

    @Override
    public BigDecimal calcularDesconto(BigDecimal valorTotal) {
        return BigDecimal.ZERO;
    }
    
}
