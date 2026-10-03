package com.sistemavendas.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sistemavendas.domain.model.Produto;

public class EstoqueService {
    private final Map<Integer, Produto> estoque = new HashMap<>();

    
    public void cadastrarProduto(Produto produto){
        if (produto == null) {
            throw new IllegalArgumentException("Produto Inválido");
        }
        if (estoque.containsKey(produto.getId())) {
           throw new IllegalArgumentException("Produto já cadastrado");
        }
        estoque.put(produto.getId(), produto);
    }

    public Optional<Produto> buscarPorId(int id){
        return Optional.ofNullable(estoque.get(id)); 
    }

    public List<Produto> obterMaisVendidos(){
        return estoque.values().stream().filter(p -> p.getQuantidadeVendida() > 0)
               .sorted(Comparator.comparingInt(Produto::getQuantidadeVendida).reversed())
               .toList();
    }

    public List<Produto> listarTodos(){
        return List.copyOf(estoque.values());
    }
}
