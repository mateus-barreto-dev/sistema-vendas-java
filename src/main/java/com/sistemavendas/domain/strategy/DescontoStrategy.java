package com.sistemavendas.domain.strategy;

import java.math.BigDecimal;

@FunctionalInterface
public interface DescontoStrategy {

    BigDecimal calcularDesconto(BigDecimal valorTotal);

    static DescontoStrategy semDesconto(){
        return valorTotal -> BigDecimal.ZERO;
    }

    static DescontoStrategy percentual(double valor){
        return valorTotal -> valorTotal.multiply(BigDecimal.valueOf(valor).divide(new BigDecimal(100)));
    }

    static DescontoStrategy progressivo(BigDecimal valorMinimo, double percentualMaior, double percentualPadrao){
       return valorTotal ->{ 
           DescontoStrategy estrategia = valorTotal.compareTo(valorMinimo) >= 0 ? 
                                            percentual(percentualMaior) : percentual(percentualPadrao);
                            
            return estrategia.calcularDesconto(valorTotal);
        };
    }
}
