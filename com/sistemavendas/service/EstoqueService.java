package com.sistemavendas.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sistemavendas.domain.exception.ProdutoNaoEncontradoException;
import com.sistemavendas.domain.model.Produto;

public class EstoqueService {
    private final Map<Integer, Produto> estoque = new HashMap<>();

    
    public void cadastrarProduto(Produto produto){
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (estoque.containsKey(produto.getId())) {
           throw new IllegalArgumentException("Produto já cadastrado");
        }
        estoque.put(produto.getId(), produto);
    }

    public Produto encontrarProduto(int id){
        return buscarPorId(id)
            .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto ainda não cadastrado"));

    }

    public void atualizarNome(int id, String nomeNovo){
        Produto produto = encontrarProduto(id);

        produto.atualizarNome(nomeNovo);

    }

    public void adicionarEstoque(int id, int quantidade){
        Produto produto = encontrarProduto(id);

        produto.adicionarEstoque(quantidade);
    }

    public void atualizarPreco(int id, BigDecimal preco){
        Produto produto = encontrarProduto(id);

        produto.atualizarPreco(preco);
    }

    public void atualizarQuantidade(int id, int quantidade){
        Produto produto = encontrarProduto(id);

        produto.atualizarQuantidade(quantidade);
    }

    public Produto excluirProduto(int id){
        return estoque.remove(id);
    }


    public List<Produto> buscar(Predicate<Produto> criterio){
        return estoque.values().stream().filter(criterio).collect(Collectors.toList());
    }


    public List<Produto> obterProdutosComEstoqueBaixo(int limite){
        return buscar(produto -> produto.getQuantidadeEstoque() < limite);
    }


    public Optional<Produto> buscarPorId(int id){
        return Optional.ofNullable(estoque.get(id)); 
    }

    public List<Produto> buscarPorNome(String nomeBuscado){
        if (nomeBuscado == null || nomeBuscado.isBlank()) {
           throw new IllegalArgumentException("Nome Inválido: O nome não pode ser nulo");
        }
        return buscar(produto -> produto.getNome().toLowerCase().contains(nomeBuscado.toLowerCase()));
    }

    public List<Produto> obterMaisVendidos(){
        return estoque.values().stream().filter(p -> p.getQuantidadeVendida() > 0)
               .sorted(Comparator.comparingInt(Produto::getQuantidadeVendida).reversed())
               .toList();
    }

    public List<Produto> listarTodosDisponiveis(){
        return estoque.values().stream().filter(produto -> produto.getQuantidadeEstoque() > 0).toList();
    }

    public List<Produto> listarTodos(){
        return List.copyOf(estoque.values());
    }


    public BigDecimal calcularValorTotalEstoque(){
        return estoque.values().stream().map(produto -> produto.getPreco()
                                                               .multiply(BigDecimal.valueOf(produto.getQuantidadeEstoque())))
                                                               .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
