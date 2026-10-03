package com.sistemavendas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.sistemavendas.domain.model.CPF;
import com.sistemavendas.domain.model.Produto;
import com.sistemavendas.domain.model.Venda;
import com.sistemavendas.domain.strategy.DescontoBonusStrategy;
import com.sistemavendas.domain.strategy.DescontoStrategy;
import com.sistemavendas.domain.strategy.SemDescontoStrategy;
import com.sistemavendas.repository.ArquivoRelatorioRepository;
import com.sistemavendas.repository.RelatorioRepository;
import com.sistemavendas.service.EstoqueService;



public class Main {

    private enum Opcoes {
        VENDA(1),
        MAIS_VENDIDOS(2),
        HISTORICO(3),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private Opcoes(int valor){
            this.valor = valor;
        }

        private static Opcoes getOp(int valor){
            for (Opcoes op : Opcoes.values()) {
                if (valor == op.valor) {
                    return op;
                }
            }
            return INVALIDO;
        }
    }
    private static CPF obterCPFValido(Scanner scanner) {
        while (true) {
            System.out.println("Informe seu CPF: xxx.xxx.xxx-xx\n");
            String inputCpf = scanner.nextLine();
            try {
                return new CPF(inputCpf);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static DescontoStrategy obterEstrategiaDesconto(Scanner scanner) {
        while (true) {
            System.out.println("Quantos pontos bônus você tem?");
            System.out.println("Caso não tenha, informe 0\n");

            String input = scanner.nextLine();
            try {
                int pontos = Integer.parseInt(input);
                if (pontos >= 10) {
                    return new DescontoBonusStrategy(pontos);
                }
                return new SemDescontoStrategy();
            } catch (NumberFormatException e) {
                System.out.println("Informe um valor numérico válido.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void adicionarProdutosNaVenda(Scanner scanner, EstoqueService estoque, Venda venda) {
        while (true) {
            System.out.println("\n--- Produtos Disponíveis ---");
            for (Produto p : estoque.listarTodos()) {
                System.out.println(p);
            }
            System.out.println("----------------------------\n");

            System.out.println("Informe o ID do produto desejado:");
            String input = scanner.nextLine();
            int id;

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido. Digite um número.");
                continue;
            }

            Optional<Produto> produtoOpt = estoque.buscarPorId(id);
            if (produtoOpt.isEmpty()) {
                System.out.println("Produto indisponível. Informe outro valor.");
                continue;
            }

            Produto produto = produtoOpt.get();

            System.out.println("Informe a quantidade desejada do produto: " + produto.getNome());
            input = scanner.nextLine();
            int quantidade;

            try {
                quantidade = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Quantidade inválida.");
                continue;
            }

            try {
                venda.adicionarItem(produto, quantidade);
                System.out.println("Item adicionado com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao adicionar item: " + e.getMessage());
                continue;
            }

            System.out.println("\nGostaria de comprar outro produto? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (!resposta.equals("S")) {
                break;
            }
        }
    }
    private static void realizarVenda(Scanner scanner, EstoqueService estoque, RelatorioRepository repository) {
        System.out.println("Bem-vindo ao Painel de Vendas!\n");

        CPF cpf = obterCPFValido(scanner);
        DescontoStrategy descontoStrategy = obterEstrategiaDesconto(scanner);

        // Percentual de imposto padrão: 10%
        Venda venda = new Venda(cpf, new BigDecimal("0.10"), descontoStrategy);

        adicionarProdutosNaVenda(scanner, estoque, venda);

        if (venda.getItens().isEmpty()) {
            System.out.println("\nNenhum item foi adicionado. Venda cancelada.\n");
            return;
        }

        System.out.println();
        String comprovante = venda.gerarComprovante();
        System.out.println(comprovante);
        repository.salvar(comprovante);
    }
    
    private static void popularEstoqueInicial(EstoqueService estoque){
        estoque.cadastrarProduto(new Produto(1, "Computador", new BigDecimal("3000.00"), 10));
        estoque.cadastrarProduto(new Produto(2, "Celular", new BigDecimal("800.00"), 30));
        estoque.cadastrarProduto(new Produto(3, "Mouse", new BigDecimal("50.00"), 100));
        estoque.cadastrarProduto(new Produto(4, "Cadeira Gamer", new BigDecimal("500.00"), 5));
    }
    
    public static void main(String[] args) {
        EstoqueService estoque = new EstoqueService();
        RelatorioRepository repository = new ArquivoRelatorioRepository("vendas.txt");
        Scanner scanner = new Scanner(System.in);
        boolean aberto = true;
        int opInt = 0;

        popularEstoqueInicial(estoque);
        

        while (aberto) {
            System.out.println("\nBem vindo a Loja");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println("1. Realizar Nova Venda\r\n" + //
                                "2. Consultar Produtos Mais Vendidos\r\n" + //
                                "3. Ver Histórico de Vendas (Arquivo)\r\n" + //
                                "0. Sair");
          
            String input = scanner.nextLine();                    
            
            try {
                opInt = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Informe um valor válido");
                continue;
            }


            Opcoes opcoes = Opcoes.getOp(opInt);

            switch (opcoes) {
                case VENDA:
                    realizarVenda(scanner, estoque, repository);
                    break;
                case MAIS_VENDIDOS:
                    List<Produto> lista_mais_vendidos = estoque.obterMaisVendidos();
                    if (lista_mais_vendidos.isEmpty()) {
                        System.out.println("\nNenhum produto vendido atualmente\n");
                        break;
                    }
                    System.out.println("\n-----Lista dos Mais Vendidos-----");
                    for (Produto produto : lista_mais_vendidos) {
                        System.out.println(produto.toString());;
                        System.out.println("Quantidade Vendida: " + produto.getQuantidadeVendida());
                        System.out.println("\n---------------------------------\n");
                    }
                    
                    break;
                case HISTORICO:
                    List<String> historico = repository.lerTodos();
                    
                    if (historico.isEmpty()) {
                        System.out.println("\nSem vendas até o momento\n");
                        break;
                    }
                    System.out.println("\n------Histórico de Vendas-----");
                    for (String s : historico) {
                        System.out.println(s);
                    }
                    System.out.println("------------------------------\n");
                    break;
                case SAIR:
                    aberto = false;
                    System.out.println("Obrigado por Comprar!\nVolte Sempre!\n");
                    break;
                case INVALIDO:
                    System.out.println("Informe uma Opção Válida");
                    break;
                default:
                    break;
            }


        }

        scanner.close();
    }

}
