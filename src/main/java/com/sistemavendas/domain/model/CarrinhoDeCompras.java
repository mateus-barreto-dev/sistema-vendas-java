package com.sistemavendas.domain.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

public class CarrinhoDeCompras {
    private final Deque<ItemVenda> carrinho = new ArrayDeque<>();


    public void adicionarItem(Produto produto, int quantidade){
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new IllegalArgumentException("Quantidade Desejada é Maior que a Quantidade Estocada");
        }
        ItemVenda item = new ItemVenda(produto, quantidade);
        carrinho.push(item);
    }

    public ItemVenda removerUltimoItem(){
        if (carrinho.isEmpty()) {
            throw new IllegalStateException("Carrinho Vazio");
        }
        return carrinho.pop();
    };
    
    public List<ItemVenda> getItens(){
        return new ArrayList<ItemVenda>(carrinho);
    }

    public Optional<ItemVenda> encontrarItem(int id){
        return carrinho.stream().filter(item -> item.getProduto().getId() == id).findFirst();
    }

    public void removerItem(ItemVenda itemVenda){
        if(itemVenda != null)
            carrinho.remove(itemVenda);
    }

    
}
