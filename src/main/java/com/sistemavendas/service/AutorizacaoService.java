package com.sistemavendas.service;

import java.util.EnumMap;
import java.util.EnumSet;

import com.sistemavendas.domain.security.Acesso;
import com.sistemavendas.domain.security.Acesso.Papel;
import com.sistemavendas.domain.security.Acesso.Permissao;

public class AutorizacaoService {
    private static EnumMap<Papel, EnumSet<Permissao>> permissoes;
    private static final EnumSet<Permissao> permissoesAdmin = EnumSet.range(Permissao.ADMINISTRAR_PRODUTOS, Permissao.REALIZAR_VENDA);
    private static final EnumSet<Permissao> permissoesCliente = EnumSet.of(Permissao.REALIZAR_VENDA);

    public AutorizacaoService(){
        permissoes = new EnumMap<>(Acesso.Papel.class);
        permissoes.put(Papel.ADMIN, permissoesAdmin);
        permissoes.put(Papel.CLIENTE, permissoesCliente);
    }

    public boolean temPermissao(Papel papel, Permissao permissao){
        
        if (!permissoes.containsKey(papel)) {
            return false;
        }
        
        return permissoes.get(papel).contains(permissao);
    }
}
