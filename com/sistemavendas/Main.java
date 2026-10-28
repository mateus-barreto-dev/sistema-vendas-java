package com.sistemavendas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Arrays;

import com.sistemavendas.domain.exception.EstoqueInsuficienteException;
import com.sistemavendas.domain.exception.ProdutoNaoEncontradoException;
import com.sistemavendas.domain.model.CPF;
import com.sistemavendas.domain.model.CarrinhoDeCompras;
import com.sistemavendas.domain.model.Cliente;
import com.sistemavendas.domain.model.ItemVenda;
import com.sistemavendas.domain.model.Produto;
import com.sistemavendas.domain.model.Venda;
import com.sistemavendas.domain.security.Acesso;
import com.sistemavendas.domain.security.Acesso.Papel;
import com.sistemavendas.domain.security.Acesso.Permissao;
import com.sistemavendas.domain.strategy.DescontoBonusStrategy;
import com.sistemavendas.domain.strategy.DescontoStrategy;
import com.sistemavendas.repository.ArquivoRelatorioRepository;
import com.sistemavendas.repository.RelatorioRepository;
import com.sistemavendas.service.AutorizacaoService;
import com.sistemavendas.service.ClienteService;
import com.sistemavendas.service.EstoqueService;
import com.sistemavendas.service.RelatorioAnaliticoService;
import com.sistemavendas.service.SessaoService;



public class Main {
    static Optional<Cliente> clienteLogado = Optional.empty();

    private enum Login {
        CADASTRAR_CLIENTE(1),
        REALIZAR_LOGIN(2),
        REALIZAR_LOGOUT(3),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private Login(int valor){
            this.valor = valor;
        }

        private static Login getOp(int valor){
            
            return Arrays.stream(Login.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }
        
    }

   

    private enum MenuHistorico {
        HISTORICO(1),
        HISTORICO_POR_CPF(2),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private MenuHistorico(int valor){
            this.valor = valor;
        }

        private static MenuHistorico getOp(int valor){
            
            return Arrays.stream(MenuHistorico.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }
        
    }

    private enum MenuMetricas {
        RELATORIO_ANALITICO(1),
        PATRIMONIO_ESTOQUE(2),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private MenuMetricas(int valor){
            this.valor = valor;
        }

        private static MenuMetricas getOp(int valor){
            
            return Arrays.stream(MenuMetricas.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }
        
    }

    private enum MenuAtualizarProduto {
        ATUALIZAR_NOME(1),
        ATUALIZAR_PRECO(2),
        ATUALIZAR_QUANTIDADE(3),
        
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private MenuAtualizarProduto(int valor){
            this.valor = valor;
        }

        private static MenuAtualizarProduto getOp(int valor){
            
            return Arrays.stream(MenuAtualizarProduto.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }
        
    }

    private enum EstoqueCRUD {
        CADASTRAR_PRODUTO(1),
        BUSCAR_PRODUTO_NOME(2),
        LISTAR_TODOS(3),
        LISTAR_BAIXO_ESTOQUE(4),
        MAIS_VENDIDOS(5),
        REPOR_ESTOQUE(6),
        ATUALIZAR_PRODUTO(7),
        REMOVER_PRODUTO(8),    
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

    private enum MenuInicial {
        MENU_LOGIN(1),
        MENU_COMPRAS(2),
        MENU_PRODUTOS(3),
        MENU_HISTORICOS(4),
        MENU_METRICAS(5),
        SAIR(0),
        INVALIDO(-1);

        private final int valor;

        private MenuInicial(int valor){
            this.valor = valor;
        }

        private static MenuInicial getOp(int valor){
            
            return Arrays.stream(MenuInicial.values())
                    .filter(opcoes -> opcoes.valor == valor)
                    .findFirst()
                    .orElse(INVALIDO);
        }

    }

    private static boolean autorizarUsuario(AutorizacaoService autorizar, Acesso.Permissao permissao){
        if (clienteLogado.isEmpty()) {
            System.out.println("Não é possível realizar esta operação sem Logar");
            return false;
        }
        if (!autorizar.temPermissao(clienteLogado.get().getPapel(), permissao)) {
            System.out.println("Você não possui permissão para esta operação");
            return false;
        }

        return true;
    }
     
    private static void menuProdutos(Scanner scanner, EstoqueService estoque, AutorizacaoService autorizar){
        if (!autorizarUsuario(autorizar, Permissao.ADMINISTRAR_PRODUTOS)) {
            return;
        }
        while (true) {
            System.out.println("\n\tBem vindo ao Menu de Produtos");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Cadastrar Produto\r\n" + 
                                "2. Buscar Produto por Nome\r\n" + 
                                "3. Listar Todos os Produtos\r\n" + 
                                "4. Listar Produtos com Baixo Estoque\r\n" + 
                                "5. Listar Mais Vendidos \r\n" + 
                                "6. Repor Estoque \r\n" + 
                                "7. Atualizar Produto \r\n" + 
                                "8. Remover Produto \r\n" + 
                                "0. Sair");

            int opInt = lerEntradaInt(scanner);
            EstoqueCRUD opcoes = EstoqueCRUD.getOp(opInt);

            switch (opcoes) {
                case CADASTRAR_PRODUTO:
                    cadastrarProduto(scanner, estoque);
                    break;
                case BUSCAR_PRODUTO_NOME:
                    buscarProdutoNome(scanner, estoque);
                    break;
                case LISTAR_TODOS:
                    listarTodos(estoque);
                    break; 
                case LISTAR_BAIXO_ESTOQUE:
                    listarProdutosEstoqueBaixo(scanner, estoque);
                    break;
                case MAIS_VENDIDOS:
                    listarMaisVendidos(estoque);
                    break;
                case REPOR_ESTOQUE:
                    reporEstoque(scanner, estoque);
                    break;
                case ATUALIZAR_PRODUTO:
                    atualizarProduto(scanner, estoque);
                    break;
                case REMOVER_PRODUTO:
                    excluirProduto(scanner, estoque);
                    break;
                case SAIR:
                    return;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }         
        }
    }


    private static void menuHistorico(Scanner scanner, RelatorioRepository repository, AutorizacaoService autorizar){
        if (!autorizarUsuario(autorizar, Permissao.CONSULTAR_HISTORICO)) {
            return;
        }

        while (true) {
            System.out.println("\n\tBem vindo ao Menu de Históricos");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Visualizar Historico Geral\r\n" + 
                                "2. Visualizar Historico por CPF\r\n" + 
                                "0. Sair");

            int opInt = lerEntradaInt(scanner);
            MenuHistorico opcoes = MenuHistorico.getOp(opInt);

            switch (opcoes) {
                case HISTORICO:
                    mostrarHistorico(repository);
                    break;
                case HISTORICO_POR_CPF:
                    mostrarHistoricoPorCPF(scanner, repository);
                    break;
                case SAIR:
                    return;
                    
                default:
                    System.out.println("Opção Inválida");
                    break;
             }
        }
    }
    private static void menuRelatorio(Scanner scanner, RelatorioAnaliticoService relatorio, EstoqueService estoque, AutorizacaoService autorizar){
        if (!autorizarUsuario(autorizar, Permissao.CONSULTAR_RELATORIOS)) {
            return;
        }

        while(true){   
            System.out.println("\n\tBem vindo ao Menu de Relatórios");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Visualizar Relatório Analítico\r\n" + 
                                "2. Visualizar Patrimônio em Estoque\r\n" + 
                                "0. Sair");
            int opInt = lerEntradaInt(scanner);
            MenuMetricas opcoes = MenuMetricas.getOp(opInt);

            switch (opcoes) {
                case RELATORIO_ANALITICO:
                    mostrarRelatorioAnalitico(relatorio);
                    break;
                case PATRIMONIO_ESTOQUE:
                    mostrarPatrimonioEstoque(estoque);
                    break;
                case SAIR:
                    return;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
        }
    }

    private static void menuLogin(Scanner scanner, SessaoService sessao, ClienteService clientes){
        while(true){   
            System.out.println("\n\tBem vindo ao Menu de Relatórios");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Cadastrar Usuário\r\n" + 
                                "2. Realizar Login\r\n" + 
                                "3. Realizar Logout\r\n" + 
                                "0. Sair");
            int opInt = lerEntradaInt(scanner);
            Login opcoes = Login.getOp(opInt);

            switch (opcoes) {
                case CADASTRAR_CLIENTE:
                    cadastrarCliente(scanner, clientes);
                    break;
                case REALIZAR_LOGIN:
                    realizarLogin(sessao, scanner);
                    break;
                case REALIZAR_LOGOUT:
                    realizarLogout(sessao, scanner);
                    break;
                case SAIR:
                    return;
                default:
                    System.out.println("Opção Inválida");
                    break;
            }
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

    private static String obterSenhaValida(Scanner scanner){
        while (true) {
            System.out.println("Informe sua Senha:\n");
            String inputSenha = scanner.nextLine();
            try {
                if (!inputSenha.isBlank()) {
                    return inputSenha;
                }
                throw new IllegalArgumentException("Senha Inválida");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
    }

    private static String obterEmailValido(Scanner scanner){
        while (true) {
            System.out.println("Informe seu Email:\n");
            String inputEmail = scanner.nextLine();
            try {
                if (inputEmail.contains("@") && inputEmail.contains(".")) {
                    return inputEmail;
                }
                throw new IllegalArgumentException("Email Inválido");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
    }

    private static String obterNomeValido(Scanner scanner){
        while (true) {
            System.out.println("Informe seu Nome:\n");
            String inputNome = scanner.nextLine();
            try {
                if (!inputNome.isBlank()) {
                    return inputNome;
                }
                throw new IllegalArgumentException("Nome Inválido");
            } catch (Exception e) {
                System.out.println("Ocorreu um erro interno inesperado no sistema!");
                System.out.println("Detalhes técnicos: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
    }

    private static Papel obterPapel(Scanner scanner){
        while (true) {
            System.out.println("Informe seu Papel:\n");
            System.out.println("1 - ADMIN");
            System.out.println("2 - CLIENTE");
            int inputPapel = lerEntradaInt(scanner);
            Papel papel = Papel.getPapel(inputPapel);

            try {
                if (!papel.equals(Papel.INVALIDO)) {
                    return papel;
                }
                throw new IllegalArgumentException("Papel Inválido");
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

    private static void adicionarProdutosNaVenda(Scanner scanner, EstoqueService estoque, Venda venda, CarrinhoDeCompras carrinhoDeCompras) {
        List<ItemVenda> itensNoCarrinho = carrinhoDeCompras.getItens();
        
        itensNoCarrinho.stream().forEach(item -> venda.adicionarItem(item.getProduto(), item.getQuantidade()));
    }
    private static void adicionarProdutoCarrinho(Scanner scanner, EstoqueService estoque, CarrinhoDeCompras carrinho){
            System.out.println("\n--- Produtos Disponíveis ---");

            estoque.listarTodosDisponiveis().forEach(System.out::println);

            System.out.println("\n----------------------------\n");

            System.out.println("Informe o ID do produto desejado:");
            int id;

       
            id = lerEntradaInt(scanner);
            Produto produto = estoque.buscarPorId(id).orElse(null);

            
            if (produto == null || produto.getQuantidadeEstoque() == 0) {
                System.out.println("Produto indisponível. Informe outro valor.");
                return;
            }

            
            int quantidadeAnterior = 0, quantidadeMaxima = -1;
            ItemVenda item = null;
            Optional<ItemVenda> itemEncontrado = carrinho.encontrarItem(id);
            if (!itemEncontrado.isEmpty()) {
                item = itemEncontrado.get();
                quantidadeAnterior = item.getQuantidade();
                quantidadeMaxima = produto.getQuantidadeEstoque() - quantidadeAnterior;
                System.out.println("Você já adicionou " + quantidadeAnterior + " exemplar(es) deste item ao carrinho");
                System.out.println("Quantidade Máxima Disponível para ser adicionada de: " + produto.getNome() + " é: " + quantidadeMaxima);
            }

            System.out.println("Informe a quantidade desejada do produto: " + produto.getNome());
            int quantidade;
            quantidade = lerEntradaInt(scanner);

            if (quantidadeMaxima != -1 && quantidade > quantidadeMaxima) {
                System.out.println("Compra Cancelada!");
                System.out.println("A quantidade Informada Supera a Quantidade Máxima");
                return;
            }


            try {
                carrinho.removerItem(item);
                carrinho.adicionarItem(produto, quantidade + quantidadeAnterior );
                System.out.println("Item adicionado com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao adicionar item: " + e.getMessage());
            }

    }

    private static void removerUltimoProdutoCarrinho(Scanner scanner, CarrinhoDeCompras carrinho){
        ItemVenda ultimoItem;
        try {
            ultimoItem = carrinho.removerUltimoItem();
        } catch (Exception e) {
            System.out.println("Não foi possível remover o Produto: " + e.getMessage());
            return;
        }
       System.out.println("Produto removido com Sucesso!");
       System.out.println("Produto Removido:\n" + ultimoItem);
    }
    private static void listarConteudoCarrinho(CarrinhoDeCompras carrinho){
        carrinho.getItens().stream().forEach(System.out::println);
    }

    private static void menuCarrinhoDeCompras(Scanner scanner, EstoqueService estoque, CarrinhoDeCompras carrinho){
        int opcao = 0;
        while (true) {
            
            System.out.println("\nBem vindo ao Carrinho de Compras!");
            System.out.println("Selecione uma das Opções abaixo:");
            System.out.println( "1. Adicionar um Produto ao Carrinho\r\n" + 
                                "2. Remover Ultimo Produto Adicionado ao Carrinho\r\n" + 
                                "3. Listar Todos os Produtos no Carrinho\r\n" + 
                                "0. Finalizar Compra");
            opcao = lerEntradaInt(scanner);
            switch (opcao) {
                case 1:
                    adicionarProdutoCarrinho(scanner,  estoque, carrinho);
                    break;
                case 2:
                    removerUltimoProdutoCarrinho(scanner, carrinho);
                    break;
                case 3:
                    listarConteudoCarrinho(carrinho);
                    break;
                case 0:
                    return;
    
                default:
                    System.out.println("Informe uma Opção válida");
                    break;
            }
        }
    }


    private static void realizarVenda(Scanner scanner, EstoqueService estoque,
         RelatorioRepository repository, RelatorioAnaliticoService relatorio, AutorizacaoService autorizar) {

        if (!autorizarUsuario(autorizar, Permissao.REALIZAR_VENDA)) {
            return;
        }

        System.out.println("Bem-vindo ao Painel de Vendas!\n");

        CPF cpf = clienteLogado.get().getCPF();
        DescontoStrategy descontoStrategy = obterEstrategiaDesconto(scanner);

        CarrinhoDeCompras carrinhoDeCompras = new CarrinhoDeCompras();
        menuCarrinhoDeCompras(scanner, estoque, carrinhoDeCompras);



        // Percentual de imposto padrão: 10%
        Venda venda = new Venda(cpf, new BigDecimal("0.10"), descontoStrategy);

        adicionarProdutosNaVenda(scanner, estoque, venda, carrinhoDeCompras);
        
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
                                "0. Sair");
          
            int input = lerEntradaInt(scanner);
            MenuAtualizarProduto op = MenuAtualizarProduto.getOp(input);

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
   
    
    private static void realizarLogin(SessaoService sessaoService, Scanner scanner){   
        CPF cpf = obterCPFValido(scanner);
        String senha = obterSenhaValida(scanner);

        clienteLogado = sessaoService.autenticar(cpf, senha);

        if (clienteLogado.isEmpty()) {
            System.out.println("Não foi possível realizar login");
            return;
        }

        System.out.println("Usuário Logado");
    }

    private static void cadastrarCliente(Scanner scanner, ClienteService clientes){
        Cliente novoCliente;
        String nome = obterNomeValido(scanner);
        CPF cpf = obterCPFValido(scanner);
        String email = obterEmailValido(scanner);
        String senha = obterSenhaValida(scanner);
        Papel papel = obterPapel(scanner);

        try {
            novoCliente = new Cliente(nome, email, senha, cpf, papel);
        } catch (Exception e) {
            System.out.println("Não foi possível criar este Usuário: " + e.getMessage());
            return;
        }

        try {
            clientes.cadastrarCliente(novoCliente);
        } catch (Exception e) {
           System.out.println("Não foi possível cadastrar este Usuário: " + e.getMessage());
           return;
        }
        
        System.out.println("Usuário Cadastrado com Sucesso");
    }

    private static void realizarLogout(SessaoService sessaoService, Scanner scanner){
        clienteLogado = Optional.empty();
        System.out.println("Logout Realizado com Sucesso");
    }
    public static void main(String[] args) {
        

        ClienteService clientes = new ClienteService();
        AutorizacaoService autorizar = new AutorizacaoService();
        SessaoService sessao = new SessaoService(10, 5, clientes);

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
            System.out.println( "1. Acessar Menu de Login\r\n" + 
                                "2. Realizar uma Venda\r\n" + 
                                "3. Acessar Menu de Produtos\r\n" + 
                                "4. Acessar Menu de Históricos \r\n" + 
                                "5. Acessar Menu de Métricas \r\n" + 
                                "0. Sair");
          
                               
            
            opInt = lerEntradaInt(scanner);
            MenuInicial opcoes = MenuInicial.getOp(opInt);

            switch (opcoes) {
                case MENU_LOGIN:
                    menuLogin(scanner, sessao, clientes);
                    break;
                case MENU_COMPRAS:
                    realizarVenda(scanner, estoque, repository, relatorio, autorizar);
                    break;
                case MENU_PRODUTOS:
                    menuProdutos(scanner, estoque, autorizar);
                    break;
                case MENU_HISTORICOS:
                    menuHistorico(scanner, repository, autorizar);
                    break;
                case MENU_METRICAS:
                    menuRelatorio(scanner, relatorio, estoque, autorizar);
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
