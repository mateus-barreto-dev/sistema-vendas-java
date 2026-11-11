package com.sistemavendas.domain.security;

import java.util.Arrays;

public class Acesso {
    public enum Papel {
        ADMIN(1),
        CLIENTE(2),
        INVALIDO(-1);

        private final int valor;

        private Papel(int valor){
            this.valor = valor;
        }

        public static Papel getPapel(int valor){
            return Arrays.stream(Papel.values())
                         .filter(papel -> papel.valor == valor)
                         .findFirst()
                         .orElse(INVALIDO);
        }
    }

    public enum Permissao {
        ADMINISTRAR_PRODUTOS(1),
        CONSULTAR_RELATORIOS(2),
        CONSULTAR_HISTORICO(3),
        REALIZAR_VENDA(4),
        INVALIDO(-1);

        private final int valor;

        private Permissao(int valor){
            this.valor = valor;
        }

        public static Permissao getOp(int valor){
            
            return Arrays.stream(Permissao.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }

        
    }
}
