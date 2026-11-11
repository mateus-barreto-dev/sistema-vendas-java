package com.sistemavendas.domain.model;

import java.util.Objects;

public class CPF {
    private final String valor;

    public CPF(String valor){

        if (valor == null) {
            throw new IllegalArgumentException("CPF inválido");
        }

        String valor_numerico = valor.replaceAll("\\D", "");

        if (!validarcpf(valor_numerico)) {
            throw new IllegalArgumentException("CPF inválido");
        }

        this.valor = valor_numerico;
    }

    private boolean validarcpf(String cpf){

        if (cpf.length() != 11) {
            return false;
        }

        int resultado = 0;
        int multiplicador = 10;
        
        for (int i = 0; i < cpf.length()-2; i++) {
            resultado += (cpf.charAt(i)-48)*multiplicador;
            multiplicador--;
        }
        
        resultado = resultado % 11;
        boolean digitoverificador1 = false;

        if (resultado < 2) {
            digitoverificador1 = cpf.charAt(9) == '0';
        }
        else
            digitoverificador1 = cpf.charAt(9) == (char)(11 - resultado + 48);

        multiplicador = 11; resultado = 0;
        for (int i = 0; i < cpf.length()-1; i++) {
            resultado += (cpf.charAt(i)-48)*multiplicador;
            multiplicador--;
        }

        resultado = resultado % 11;

        boolean digitoverificador2 = false;
        if (resultado < 2) {
            digitoverificador2 = cpf.charAt(10) == '0';
        }
        else
            digitoverificador2 = cpf.charAt(10) == (char)(11 - resultado + 48);

        return digitoverificador1 && digitoverificador2;
    }

    public String getValor(){
        return this.valor;
    }

    public String getFormatado(){
        return this.valor.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    @Override 
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (!(o instanceof CPF)) {
            return false;
        }

        CPF cpf = (CPF)o;

        return this.valor.equals(cpf.getValor());
    }

    @Override 
    public int hashCode(){
        return Objects.hash(valor);
    }
    
}
