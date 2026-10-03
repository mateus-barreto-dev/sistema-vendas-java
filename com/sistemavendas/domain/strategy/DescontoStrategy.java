package com.sistemavendas.domain.strategy;

import java.math.BigDecimal;

@FunctionalInterface
public interface DescontoStrategy {

    BigDecimal calcularDesconto(BigDecimal valorTotal);

}
