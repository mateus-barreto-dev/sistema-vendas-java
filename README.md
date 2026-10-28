🛒 Sistema de Vendas em Java Moderno (CLI)
Um sistema robusto de gerenciamento de estoque, terminal de vendas, controle de acesso baseado em papéis (RBAC), sessões ativas e emissão de relatórios analíticos em linha de comando (CLI), desenvolvido em Java 17+.

O projeto passou por uma evolução arquitetural completa, transicionando de um modelo procedural inicial para uma estrutura orientada a objetos de nível corporativo. Ele integra rigorosamente **Estruturas de Dados e Collections avançadas**, princípios de **Clean Code**, **SOLID**, **Design Patterns** e construções idiomáticas de **Programação Funcional** (Stream API, Optional, Predicates e Lambdas).

📌 Sumário
- Destaques da Refatoração
- Tecnologias e Conceitos Avançados de Collections Aplicados
- Padrões de Projeto & Princípios SOLID
- Arquitetura do Projeto
- Funcionalidades e Recursos
- Tratamento de Exceções e Resiliência
- Como Executar o Projeto
- Roteiro de Evolução Futura

🔄 Destaques da Refatoração
| Aspecto | Versão Procedural (Anterior) | Versão Atual (Refatorada & Avançada) |
| :--- | :--- | :--- |
| **Estrutura de Dados** | Arrays paralelos de tamanho fixo. | Coleções dinâmicas de altíssimo desempenho (`HashMap`, `EnumMap`, `EnumSet`, `LinkedHashMap`, `ArrayDeque`). |
| **Segurança e Acesso** | Sem controle de usuários ou sessões. | Controle de acesso via RBAC (`EnumMap<Papel, EnumSet<Permissao>>`) e Sessões Ativas controladas por LRU Cache. |
| **Design de Código** | Lógica centralizada, acoplada e imperativa. | Responsabilidades separadas em camadas estritas (Domínio, Segurança, Serviços, Repositório e CLI). |
| **Integridade de Dados** | Mutações diretas sem regras centralizadas. | Validações encapsuladas via Value Objects (`CPF`), senhas e tratamento defensivo. |
| **Padrões de Busca** | Iterações manuais com laços `for`. | Processamento declarativo com Stream API e expressões Lambda. |
| **Tratamento de Erros** | Respostas genéricas e interrupções. | Exceções de domínio personalizadas e loops resilientes no terminal. |

🛠️ Tecnologias e Conceitos Avançados de Collections Aplicados
- **Linguagem & Runtime:** Java 17+
- **Paradigma Principal:** Orientação a Objetos combinada com Programação Funcional e Estruturas de Dados Avançadas.
- **Coleções de Alta Performance Utilizadas:**
  - `HashMap<CPF, Cliente>` e `HashMap<Integer, Produto>`: Garantia de buscas, inserções e validações em tempo constante $O(1)$ no gerenciamento de contas e estoque.
  - `EnumMap<Papel, EnumSet<Permissao>>`: Mapeamento de controle de acesso (RBAC) extremamente performático baseado internamente em arrays contínuos e operações bitwise, sem colisões de hash.
  - `LinkedHashMap` (LRU Cache): Gerenciamento de sessões ativas no terminal configurado com ordem de acesso (`accessOrder = true`) e remoção automática do usuário ocioso mais antigo ao atingir o limite de capacidade.
  - `Deque<ItemVenda>` via `ArrayDeque`: Implementação de Carrinho de Compras estruturado como Pilha (LIFO), permitindo operações de adição e desfazer (*Undo*) em $O(1)$ sem overhead de memória.
- **Recursos Avançados do Java Moderno:**
  - **Stream API & Collectors:** Filtros dinâmicos, ordenações encadeadas com `Comparator` e agregações financeiras com `.reduce()`.
  - **Optional API:** Manipulação declarativa da presença/ausência de entidades, eliminando o risco de `NullPointerException`.
  - **Imutabilidade Defensiva:** Blindagem de coleções expostas utilizando `List.copyOf()` para impedir mutações externas indevidas.
  - **BigDecimal & RoundingMode:** Manipulação de precisão financeira exata em preços, impostos, descontos e métricas de patrimônio.

🏗️ Padrões de Projeto & Princípios SOLID
### Padrões de Projeto (Design Patterns)
- **Strategy Pattern (`DescontoStrategy`):** Interface funcional para cálculo de desconto dinâmico no momento da venda (`DescontoBonusStrategy` e `SemDescontoStrategy`), desacoplando regras de negócio.
- **Repository Pattern (`RelatorioRepository`):** Abstração da camada de persistência (`ArquivoRelatorioRepository`), permitindo transição transparente entre arquivos de texto e bancos de dados futuros.

### Princípios SOLID Aplicados
- **Single Responsibility Principle (SRP):** Cada classe possui um único propósito (ex: `SessaoService` gerencia o ciclo de vida de login/LRU Cache, `AutorizacaoService` gerencia permissões).
- **Open/Closed Principle (OCP):** Novas regras de desconto, papéis de acesso ou filtros podem ser adicionados sem modificar o código existente.
- **Dependency Inversion Principle (DIP):** Módulos de alto nível dependem de abstrações (interfaces e contratos) e não de implementações concretas.

📂 Arquitetura do Projeto
```text
com.sistemavendas/
├── domain/
│   ├── exception/         # Exceções customizadas de domínio
│   │   ├── ClienteJaCadastradoException.java
│   │   ├── EstoqueInsuficienteException.java
│   │   └── ProdutoNaoEncontradoException.java
│   ├── model/             # Entidades e Value Objects do Domínio
│   │   ├── CarrinhoDeCompras.java (ArrayDeque - LIFO)
│   │   ├── Cliente.java
│   │   ├── CPF.java
│   │   ├── ItemVenda.java
│   │   ├── Produto.java
│   │   └── Venda.java
│   ├── security/          # Regras de Segurança e Acessos
│   │   └── Acesso.java    # Enums de Papéis e Permissões (RBAC)
│   └── strategy/          # Design Pattern Strategy
│       ├── DescontoBonusStrategy.java
│       ├── DescontoStrategy.java
│       └── SemDescontoStrategy.java
├── repository/            # Camada de Persistência e Arquivos
│   ├── ArquivoRelatorioRepository.java
│   └── RelatorioRepository.java
├── service/               # Camada de Serviços e Regras de Negócio
│   ├── AutorizacaoService.java # Validação baseada em EnumMap/EnumSet
│   ├── ClienteService.java     # Gerenciamento de contas via HashMap
│   ├── EstoqueService.java     # Gerenciamento de estoque
│   ├── RelatorioAnaliticoService.java
│   └── SessaoService.java      # LRU Cache de Sessões via LinkedHashMap
└── Main.java              # Interface CLI, Menus com Enums e Controladores de Sessão
```

🚀 Funcionalidades e Recursos
1. **Segurança, Autenticação e Sessões (ClienteService / SessaoService / AutorizacaoService)**
   - Cadastro rigoroso de clientes com validação de formato de CPF, e-mail, senha e atribuição de Papéis (`ADMIN` ou `CLIENTE`).
   - Autenticação e controle de sessões ativas simultâneas utilizando um LRU Cache baseada em `LinkedHashMap`.
   - Autorização baseada em papéis (RBAC) utilizando `EnumMap` e `EnumSet`, blindando o acesso a menus administrativos e operacionais.

2. **Carrinho de Compras Interativo (CarrinhoDeCompras / ArrayDeque)**
   - Montagem de pedidos flexível em formato de Pilha (LIFO).
   - Capacidade de Desfazer (*Undo*) a última adição de item de forma instantânea em $O(1)$.
   - Validação inteligente de quantidade máxima acumulada em relação ao saldo físico do estoque.

3. **Gestão e Operações de Estoque (EstoqueService)**
   - Cadastro, reposição e atualização granular (CRUD de nome, preço e quantidade).
   - Busca parametrizada por nome e mapeamento de produtos com estoque abaixo de um limite configurável.
   - Ranking automático de Produtos Mais Vendidos e cálculo do patrimônio total estocado via `BigDecimal`.

4. **Terminal de Vendas, Checkout e Auditoria (Venda / Main / RelatorioRepository)**
   - Checkout validando permissões de usuário logado.
   - Aplicação de impostos e estratégias de desconto por pontos bônus.
   - Baixa definitiva em lote no estoque físico e geração detalhada de comprovantes gravados de forma append-only em `vendas.txt`.

🛡️ Tratamento de Exceções e Resiliência
- **Exceções de Domínio Customizadas:** Mensagens claras para cenários como CPFs duplicados, estoque insuficiente, produtos não encontrados e falhas de autenticação.
- **Entrada Resiliente no Terminal:** Métodos de leitura blindados (`lerEntradaInt` e `lerEntradaBigDecimal`) com tratamento de `NumberFormatException` em loops contínuos, impedindo falhas por digitação incorreta.

⚙️ Como Executar o Projeto
### Pré-requisitos
- Java Development Kit (JDK) 17 ou superior instalado.
- Terminal / Prompt de Comando.

### Passo a Passo
1. Clone o repositório:
```bash
git clone https://github.com/mateus-barreto-dev/sistema-vendas-java.git
```
2. Navegue até o diretório do projeto:
```bash
cd sistema-vendas-java
```
3. Compile os arquivos fonte:
```bash
javac -d bin com/sistemavendas/Main.java com/sistemavendas/**/*.java
```
4. Execute a aplicação:
```bash
java -cp bin com.sistemavendas.Main
```
*(Nota no Windows cmd/PowerShell: use `java -cp bin com.sistemavendas.Main`)*

🔮 Roteiro de Evolução Futura
- [ ] **Testes Unitários e Mocking:** Implementação de suítes de testes automatizados utilizando JUnit 5 e Mockito para cobrir serviços e regras de negócio.
- [ ] **Integração com Banco de Dados Relacional:** Substituição da persistência em arquivo plano (`vendas.txt`) por PostgreSQL/MySQL utilizando Spring Data JPA / Hibernate.
- [ ] **API RESTful com Spring Boot:** Migração do terminal CLI para uma arquitetura web moderna baseada em controladores HTTP, DTOs e validações corporativas.
