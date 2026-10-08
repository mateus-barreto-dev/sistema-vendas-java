package com.sistemavendas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

import com.sistemavendas.domain.exception.EstoqueInsuficienteException;
import com.sistemavendas.domain.exception.ProdutoNaoEncontradoException;
import com.sistemavendas.domain.model.CPF;
import com.sistemavendas.domain.model.Produto;
import com.sistemavendas.domain.model.Venda;
import com.sistemavendas.domain.strategy.DescontoBonusStrategy;
import com.sistemavendas.domain.strategy.DescontoStrategy;
import com.sistemavendas.domain.strategy.SemDescontoStrategy;
import com.sistemavendas.repository.ArquivoRelatorioRepository;
import com.sistemavendas.repository.RelatorioRepository;
import com.sistemavendas.service.EstoqueService;
import com.sistemavendas.service.RelatorioAnaliticoService;



public class Main {

    private enum Opcoes {
        VENDA(1),
        MAIS_VENDIDOS(2),
        HISTORICO(3),
        HISTORICO_POR_CPF(4),
        CADASTRAR_PRODUTO(5),
        REPOR_ESTOQUE(6),
        ATUALIZAR_PRODUTO(7),
        EXCLUIR_PRODUTO(8),
        BUSCAR_PRODUTO_NOME(9),
        RELATORIO_ANALITICO(10),
        PATRIMONIO_ESTOQUE(11),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private Opcoes(int valor){
            this.valor = valor;
        }

        private static Opcoes getOp(int valor){
            
            return Arrays.stream(Opcoes.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }

    }

    private enum EstoqueCRUD {
        ATUALIZAR_NOME(1),
        ATUALIZAR_PRECO(2),
        ATUALIZAR_QUANTIDADE(3),
        LISTAR_TODOS(4),
        LISTAR_BAIXO_ESTOQUE(5),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private EstoqueCRUD(int valor){
            this.valor = valor;
        }

        private static EstoqueCRUD getOp(int valor){
            
            return Arrays.stream(EstoqueCRUD.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }

    }

    private static void listarTodos(EstoqueService estoque){
        estoque.listarTodos().forEach(System.out::println);
    }

    private static void listarMaisVendidos(EstoqueService estoque){
        List<Produto> lista_mais_vendidos = estoque.obterMaisVendidos();
        if (lista_mais_vendidos.isEmpty()) {
            System.out.println("\nNenhum produto vendido atualmente\n");
            return;
        }
        System.out.println("\n-----Lista dos Mais Vendidos-----");
        lista_mais_vendidos.forEach(p -> System.out.println(p.toString() +
                                        "\n"+"Quantidade Vendida: " + p.getQuantidadeVendida() +
                                        "\n---------------------------------\n")
                                    );
    } 

    private static void mostrarHistorico(RelatorioRepository repository){
        List<String> historico = repository.lerTodos();
        
        if (historico.isEmpty()) {
            System.out.println("\nSem vendas até o momento\n");
           return;
        }

        System.out.println("\n------Histórico de Vendas-----");
        historico.forEach(System.out::println);
        System.out.println("\n------------------------------\n");
    }

    private static  int lerEntradaInt(Scanner scanner){
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e){
                System.out.println("Valor Inválido. Tente Novamente");
            }
              catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
        
        
    }

    private static BigDecimal lerEntradaBigDecimal(Scanner scanner){

        BigDecimal preco;
        while (true) {
            try {
                preco = new BigDecimal(scanner.nextLine().replace(",", ".").trim());
                return preco;
            } catch (NumberFormatException e){
                System.out.println("Valor Inválido. Tente Novamente");
            }
              catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            } 
        }
        
        
    }


    private static CPF obterCPFValido(Scanner scanner) {
        while (true) {
            System.out.println("Informe seu CPF: xxx.xxx.xxx-xx\n");
            String inputCpf = scanner.nextLine();
            try {
                return new CPF(inputCpf);
            } catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
    }

    private static DescontoStrategy obterEstrategiaDesconto(Scanner scanner) {
        while (true) {
            System.out.println("Quantos pontos bônus você tem?");
            System.out.println("Caso não tenha, informe 0\n");

            int pontos = lerEntradaInt(scanner);
            
            return pontos >= 10 ? new DescontoBonusStrategy(pontos) : DescontoStrategy.semDesconto();
           
        }
    }

    private static void adicionarProdutosNaVenda(Scanner scanner, EstoqueService estoque, Venda venda) {
        while (true) {
            System.out.println("\n--- Produtos Disponíveis ---");

            estoque.listarTodosDisponiveis().forEach(System.out::println);

            System.out.println("\n----------------------------\n");

            System.out.println("Informe o ID do produto desejado:");
            int id;

       
            id = lerEntradaInt(scanner);
            Produto produto = estoque.buscarPorId(id).orElse(null);

            
            if (produto == null || produto.getQuantidadeEstoque() == 0) {
                System.out.println("Produto indisponível. Informe outro valor.");
                continue;
            }

            System.out.println("Informe a quantidade desejada do produto: " + produto.getNome());
            int quantidade;

          
            quantidade = lerEntradaInt(scanner);
            

            try {
                venda.adicionarItem(produto, quantidade);
                System.out.println("Item adicionado com sucesso!");
            } catch (EstoqueInsuficienteException | ProdutoNaoEncontradoException e) {
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
    private static void realizarVenda(Scanner scanner, EstoqueService estoque,
         RelatorioRepository repository, RelatorioAnaliticoService relatorio) {
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
        relatorio.addVenda(venda);
        repository.salvar(comprovante);
    }

    private static void mostrarHistoricoPorCPF(Scanner scanner, RelatorioRepository repository){
        CPF cpf = obterCPFValido(scanner);
        List<String> comprovantes = repository.buscarNoHistorico(linha -> linha.contains(cpf.getFormatado()));
        
        if (comprovantes.isEmpty()) {
            System.out.println("Este CPF nunca realizou nenhuma compra");
            return;
        }

        comprovantes.forEach(System.out::println);
    }

    private static void cadastrarProduto(Scanner scanner, EstoqueService estoque){
        System.out.println("Informe o Nome do Produto");
        String nome = scanner.nextLine();
        System.out.println("Informe o preço do produto");
        BigDecimal preco = lerEntradaBigDecimal(scanner);
        System.out.println("Informe a Quantidade do Produto");
        int quantidade = lerEntradaInt(scanner);
        listarTodos(estoque);
        System.out.println("Informe o ID do Produto");
        System.out.println("LEMBRETE: O ID do produto deve ser diferente de todos os IDs já cadastrados");
        int id;
            while (true) {
                id = lerEntradaInt(scanner);
                if (estoque.buscarPorId(id).isEmpty()) {
                    break;
                }
                System.out.println("ID já cadastrado! Informe um ID diferente:");
            }

        try {
            Produto produto = new Produto(id, nome, preco, quantidade);
            estoque.cadastrarProduto(produto);
            System.out.println("Produto Cadastrado com SUCESSO!");
            estoque.buscarPorId(id).ifPresent(System.out::println);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar produto: " + e.getMessage());
            System.out.println("Retornando ao Menu Principal...");
        }

    }
    private static void reporEstoque(Scanner scanner, EstoqueService estoque){
        int id; int quantidade;
        System.out.println("Produtos Cadastrados");
        listarTodos(estoque);
        System.out.println("Informe o ID do produto buscado");
        id = lerEntradaInt(scanner);
        System.out.println("Informe a quantidade a ser adicionada");
        quantidade = lerEntradaInt(scanner);

        try {
            estoque.adicionarEstoque(id, quantidade);
        } catch (Exception e) {
            System.out.println("ERRO: Não Foi Possível Atualizar O Estoque");
            System.out.println(e.getMessage());
            System.out.println("Retornando Para o Menu Inicial...");
            return;
        }
        System.out.println("Produto Atualizado com Sucesso!");
    }

    private static void atualizarNome(Scanner scanner, EstoqueService estoque){
        System.out.println("Informe o ID do produto");
        int id = lerEntradaInt(scanner);
        System.out.println("Informe o novo Nome");
        String nome = scanner.nextLine();

        try {
            estoque.atualizarNome(id, nome);
            System.out.println("O Nome foi Atualizado com Sucesso");
        } catch (Exception e) {
            System.out.println("Não foi possível atualizar o Nome");
            System.out.println("ERRO: " + e.getMessage());
        }
        estoque.buscarPorId(id).ifPresent(System.out::println);
    }
    private static void atualizarPreco(Scanner scanner, EstoqueService estoque){
        System.out.println("Informe o ID do produto");
        int id = lerEntradaInt(scanner);
        System.out.println("Informe o novo Preco");
        BigDecimal preco = lerEntradaBigDecimal(scanner);

        try {
            estoque.atualizarPreco(id, preco);
            System.out.println("O Preço foi Atualizado com Sucesso");
        } catch (Exception e) {
            System.out.println("Não foi possível atualizar o Preço");
            System.out.println("ERRO: " + e.getMessage());
        }
        estoque.buscarPorId(id).ifPresent(System.out::println);
    }
    private static void atualizarQuantidade(Scanner scanner, EstoqueService estoque){
        System.out.println("Informe o ID do produto");
        int id = lerEntradaInt(scanner);
        System.out.println("Informe a nova Quantidade");
        int quantidade = lerEntradaInt(scanner);

        try {
            estoque.atualizarQuantidade(id, quantidade);
            System.out.println("A quantidade foi Atualizada com Sucesso");
        } catch (Exception e) {
            System.out.println("Não foi possível atualizar a quantidade");
            System.out.println("ERRO: " + e.getMessage());
        }
        estoque.buscarPorId(id).ifPresent(System.out::println);
    }
    private static void listarProdutosEstoqueBaixo(Scanner scanner,EstoqueService estoque){
        System.out.println("Informe um valor para listar todos os produtos com a quantidade em estoque abaixo deste valor");
        int valor = lerEntradaInt(scanner);
        List<Produto> produtos = estoque.obterProdutosComEstoqueBaixo(valor);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum Produto Abaixo Deste Valor");
            return;
        }
        produtos.stream().forEach(System.out::println);
    }




    private static void atualizarProduto(Scanner scanner, EstoqueService estoque){
        boolean atualizando = true;
        while (atualizando) {
            System.out.println("\nBem vindo ao Menu de Atualização de Produtos");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Atualizar Nome\r\n" + 
                                "2. Atualizar Preço\r\n" + 
                                "3. Atualizar Quantidade Estocada\r\n" + 
                                "4. Listar Todos os Produtos \r\n" + 
                                "5. Listar Produtos com Estoque Baixo \r\n" + 
                                "0. Sair");
          
            int input = lerEntradaInt(scanner);
            EstoqueCRUD op = EstoqueCRUD.getOp(input);

            switch (op) {
                case ATUALIZAR_NOME:
                    atualizarNome(scanner, estoque);
                    break;
                case ATUALIZAR_PRECO:
                    atualizarPreco(scanner, estoque);
                    break;    
                case ATUALIZAR_QUANTIDADE:
                    atualizarQuantidade(scanner, estoque);
                    break; 
                case LISTAR_TODOS:
                    listarTodos(estoque);
                    break; 
                case LISTAR_BAIXO_ESTOQUE:
                    listarProdutosEstoqueBaixo(scanner, estoque);
                    break;  
                case SAIR:
                    atualizando = false;
                    break;           
                default:
                    break;
            }
        }

    }
    private static void buscarProdutoNome(Scanner scanner, EstoqueService estoque){
        List<Produto> produtos;
        System.out.println("Informe o Nome do Produto Desejado");
        String nome = scanner.nextLine();
        try {
            produtos = estoque.buscarPorNome(nome);
        } catch (Exception e) {
            System.out.println("Não foi Possível Buscar pelos Produtos");
            System.out.println("ERRO: " + e.getMessage());
            return;
        }
        if (produtos.isEmpty()) {
            System.out.println("Nenhum Produto Encontrado");
            return;
        }
        System.out.println("\nProdutos Encontrados:");
        produtos.stream().forEach(System.out::println);
    }

    private static void excluirProduto(Scanner scanner, EstoqueService estoque){
        listarTodos(estoque);
        System.out.println("Informe o ID do produto");
        int id = lerEntradaInt(scanner);
        try {
            Produto produto = estoque.excluirProduto(id);
            System.out.println("Produto Excluído com Sucesso");
            System.out.println(produto);
        } catch (Exception e) {
            System.out.println("Não foi possível excluir o produto");
            System.out.println("ERRO: " + e.getMessage());
        }
        
    }


    private static void mostrarRelatorioAnalitico(RelatorioAnaliticoService relatorio){
        System.out.println("Total Vendido Até Agora: R$" + relatorio.calcularReceitaTotal());
        System.out.println("Valor Médio Gasto em uma Venda: R$" + relatorio.calcularRendaMedia());
    }
    private static void mostrarPatrimonioEstoque(EstoqueService estoque){
        System.out.println("Patrimônio Total em Estoque: R$ " + estoque.calcularValorTotalEstoque());
    }
    
    
    private static void popularEstoqueInicial(EstoqueService estoque){
        estoque.cadastrarProduto(new Produto(1, "Computador", new BigDecimal("3000.00"), 10));
        estoque.cadastrarProduto(new Produto(2, "Celular", new BigDecimal("800.00"), 30));
        estoque.cadastrarProduto(new Produto(3, "Mouse", new BigDecimal("50.00"), 100));
        estoque.cadastrarProduto(new Produto(4, "Cadeira Gamer", new BigDecimal("500.00"), 5));
        estoque.cadastrarProduto(new Produto(5, "Roteador Wi-Fi", new BigDecimal("100.00"), 0));
    }
    
    public static void main(String[] args) {
        RelatorioAnaliticoService relatorio = new RelatorioAnaliticoService();
        EstoqueService estoque = new EstoqueService();
        RelatorioRepository repository = new ArquivoRelatorioRepository("vendas.txt");
        Scanner scanner = new Scanner(System.in);
        boolean aberto = true;
        int opInt = 0;

        popularEstoqueInicial(estoque);
        

        while (aberto) {
            System.out.println("\n\tBem vindo a Loja");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println("1. Realizar Nova Venda\r\n" + 
                                "2. Consultar Produtos Mais Vendidos\r\n" + 
                                "3. Ver Histórico de Vendas (Arquivo)\r\n" + 
                                "4. Ver Histórico por CPF \r\n" + 
                                "5. Cadastrar Novo Produto \r\n" + 
                                "6. Repor Estoque de um Produto \r\n" + 
                                "7. Atualizar Produto \r\n" + 
                                "8. Excluir Produto \r\n" + 
                                "9. Buscar Produto pelo Nome \r\n" + 
                                "10. Ver Relatório Analítico \r\n" + 
                                "11. Consultar o Patrimônio do Estoque \r\n" + 
                                "0. Sair");
          
                               
            
            opInt = lerEntradaInt(scanner);
            Opcoes opcoes = Opcoes.getOp(opInt);

            switch (opcoes) {
                case VENDA:
                    realizarVenda(scanner, estoque, repository, relatorio);
                    break;
                case MAIS_VENDIDOS:
                    listarMaisVendidos(estoque);
                    break;
                case HISTORICO:
                    mostrarHistorico(repository);
                    break;
                case HISTORICO_POR_CPF:
                    mostrarHistoricoPorCPF(scanner, repository);
                    break;
                case CADASTRAR_PRODUTO:
                    cadastrarProduto(scanner, estoque);
                    break;
                case REPOR_ESTOQUE:
                    reporEstoque(scanner, estoque);
                    break;
                case ATUALIZAR_PRODUTO:
                    atualizarProduto(scanner, estoque);
                    break;
                case EXCLUIR_PRODUTO:
                    excluirProduto(scanner, estoque);
                    break;
                case BUSCAR_PRODUTO_NOME:
                    buscarProdutoNome(scanner, estoque);
                    break;
                case RELATORIO_ANALITICO:
                    mostrarRelatorioAnalitico(relatorio);
                    break;
                case PATRIMONIO_ESTOQUE:
                    mostrarPatrimonioEstoque(estoque);
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
