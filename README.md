# 🛒 Sistema de Vendas em Java Moderno (CLI)

Um sistema de gerenciamento de estoque, terminal de vendas e emissão de relatórios analíticos em linha de comando (CLI), desenvolvido em Java 17+.

O projeto passou por uma evolução arquitetural completa, transicionando de um modelo inicial procedural para uma estrutura orientada a objetos robusta, integrando princípios de **Clean Code**, **SOLID**, **Design Patterns** e construções idiomáticas de **Programação Funcional** (*Stream API*, *Optional*, *Predicates* e *Lambdas*).

---

## 📌 Sumário
- [Destaques da Refatoração](#-destaques-da-refatoração)
- [Tecnologias e Conceitos Aplicados](#-tecnologias-e-conceitos-aplicados)
- [Padrões de Projeto & Princípios SOLID](#-padrões-de-projeto--princípios-solid)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Funcionalidades e Recursos](#-funcionalidades-e-recursos)
- [Tratamento de Exceções e Resiliência](#-tratamento-de-exceções-e-resiliência)
- [Como Executar o Projeto](#-como-executar-o-projeto)
- [Roteiro de Evolução Futura](#-roteiro-de-evolução-futura)

---

## 🔄 Destaques da Refatoração

| Aspecto | Versão Procedural (Anterior) | Versão Atual (Refatorada) |
| :--- | :--- | :--- |
| **Estrutura de Dados** | Arrays paralelos de tamanho fixo. | Coleções dinâmicas de alto desempenho (`HashMap<Integer, Produto>`, `List`). |
| **Design de Código** | Lógica centralizada, acoplada e imperativa. | Responsabilidades separadas em camadas (Domínio, Serviços, Repositório e CLI). |
| **Integridade de Dados** | Mutações diretas sem regras centralizadas. | Validações encapsuladas via *Value Objects* (`CPF`) e tratamento defensivo. |
| **Padrões de Busca** | Iterações de laço `for` manuais. | Processamento declarativo com *Stream API* e expressões *Lambda*. |
| **Tratamento de Erros** | Respostas genéricas e interrupções inesperadas. | Exceções de domínio personalizadas e *handly loop* defensivo no CLI. |

---

## 🛠️ Tecnologias e Conceitos Aplicados

* **Linguagem & Runtime:** Java 17+
* **Paradigma Principal:** Orientação a Objetos combinada com Programação Funcional.
* **Coleções e Estruturas de Dados:** 
  * `HashMap<Integer, Produto>` no `EstoqueService` para buscas, inserções e atualizações em tempo constante $O(1)$.
  * Imutabilidade defensiva com `List.copyOf()` e métodos `.toList()` no retorno de consultas.
* **Recursos Avançados do Java Moderno:**
  * **Stream API & Collectors:** Filtros dinâmicos, ordenações encadeadas com `Comparator.comparingInt(...).reversed()` e agregações financeiras com `.reduce()`.
  * **Optional API:** Manipulação declarativa da presença/ausência de entidades com `Optional<Produto>`, eliminando o risco de `NullPointerException` e evitando verificações explícitas `if (x != null)`.
  * **Predicate<T>:** Uso de predicados funcionais para consultas parametrizadas flexíveis reutilizáveis.
  * **BigDecimal & RoundingMode:** Manipulação de precisão financeira exata em preços, taxas de impostos, cálculos de patrimônio e renda média.

---

## 🏗️ Padrões de Projeto & Princípios SOLID

### Padrões de Projeto (Design Patterns)
1. **Strategy Pattern (`DescontoStrategy`):**
   * Interface funcional para cálculo de desconto dinâmico no momento da venda.
   * Implementações desacopladas (`DescontoBonusStrategy` e `SemDescontoStrategy`), permitindo que novas regras de desconto sejam adicionadas sem alterar o fluxo da classe `Venda` ou da `Main`.
2. **Repository Pattern (`RelatorioRepository`):**
   * Abstração da camada de persistência. A interface permite gravar e consultar históricos sem que as regras de negócio saibam se os dados estão sendo gravados em um arquivo texto (`ArquivoRelatorioRepository`) ou em um banco de dados futuro.

### Princípios SOLID Aplicados
* **Single Responsibility Principle (SRP):** Cada classe possui um propósito único. A `Main` lida estritamente com interação do usuário; `EstoqueService` gerencia estado e mutação de produtos; `RelatorioAnaliticoService` foca em métricas de vendas.
* **Open/Closed Principle (OCP):** Novas estratégias de desconto e novos critérios de filtragem de produtos (`Predicate`) podem ser adicionados sem modificar o código existente nos serviços.
* **Dependency Inversion Principle (DIP):** Modulos de alto nível (`Main`, `Venda`) dependem de abstrações (`RelatorioRepository`, `DescontoStrategy`) em vez de implementações concretas.

---

## 📂 Arquitetura do Projeto

```text
com.sistemavendas/
├── domain/
│   ├── exception/         # Exceções customizadas de domínio
│   │   ├── EstoqueInsuficienteException.java
│   │   └── ProdutoNaoEncontradoException.java
│   ├── model/             # Entidades e Value Objects do Domínio
│   │   ├── CPF.java
│   │   ├── ItemVenda.java
│   │   ├── Produto.java
│   │   └── Venda.java
│   └── strategy/          # Design Pattern Strategy
│       ├── DescontoBonusStrategy.java
│       ├── DescontoStrategy.java
│       └── SemDescontoStrategy.java
├── repository/            # Camada de Persistência e Arquivos
│   ├── ArquivoRelatorioRepository.java
│   └── RelatorioRepository.java
├── service/               # Camada de Serviços e Regras de Negócio
│   ├── EstoqueService.java
│   └── RelatorioAnaliticoService.java
└── Main.java              # Interface CLI, Menus com Enums e Controladores
```

## 🚀 Funcionalidades e Recursos

### 1. Gestão e Operações de Estoque (`EstoqueService`)
* **Cadastro e Reposição:** Cadastro de produtos garantindo unicidade e atualização de saldo em estoque.
* **Atualização Granular (CRUD):** Edição individual de Nome, Preço e Quantidade de qualquer item cadastrado.
* **Busca e Filtragem Funcional:**
  * Busca parametrizada por nome (`toLowerCase().contains()`).
  * Mapeamento de produtos com estoque abaixo de um limite configurável.
  * Ranking automático de **Produtos Mais Vendidos** por volume acumulado.
* **Métricas Financeiras:** Cálculo consolidado do valor total do patrimônio estocado usando `BigDecimal`.

### 2. Terminal de Vendas e Checkout (`Venda` / `Main`)
* **Validação Rígida de CPF:** Garantia do formato legal do CPF (`xxx.xxx.xxx-xx`) encapsulado no *Value Object* `CPF`.
* **Cálculo de Impostos e Descontos:** Aplicação encadeada de taxa tributária percentual e políticas funcionais de desconto por pontos acumulados.
* **Baixa Automática:** Verificação de disponibilidade física e redução imediata de quantidade no estoque no ato da compra.
* **Geração de Comprovante:** Impressão detalhada dos itens, valores unitários, subtotal, impostos, descontos aplicados e valor final.

### 3. Persistência e Relatórios Analíticos (`RelatorioAnaliticoService`)
* **Relatório no Console:** Consulta de Receita Total Acumulada e Ticket Médio por Venda (com arredondamento `HALF_UP`).
* **Auditoria por Arquivo Textual:** Gravação e leitura append-only de comprovantes no arquivo `vendas.txt`.
* **Consulta por CPF:** Leitura funcional com filtros de histórico baseados no CPF do cliente via repositório.

---

## 🛡️ Tratamento de Exceções e Resiliência

A aplicação foi projetada para ser tolerante a falhas na entrada de dados do usuário e nas regras de negócio:

1. **Exceções Customizadas de Domínio:**
   * `ProdutoNaoEncontradoException`: Disparada ao consultar ou modificar IDs inexistentes.
   * `EstoqueInsuficienteException`: Impedimento imediato de vendas com quantidade solicitada maior do que a disponível.
2. **Entrada de Dados Resiliente no Terminal:**
   * Métodos dedicados (`lerEntradaInt` e `lerEntradaBigDecimal`) com tratamento de `NumberFormatException` em loops contínuos, garantindo que digitações alfabéticas em campos numéricos não interrompam o programa.
3. **Mapeamento de Opções via Enum (`Opcoes` e `EstoqueCRUD`):**
   * Mapeamento numérico com tratamento de estado inválido (`INVALIDO`), garantindo tratamento gracioso para opções inexistentes no menu.

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
* **Java Development Kit (JDK) 17** ou superior instalado.
* Terminal/Prompt de Comando.

### Passo a Passo

1. **Clone o repositório:**

```bash
git clone https://github.com/mateus-barreto-dev/sistema-vendas-java.git
```

2. **Navegue até o diretório do projeto:**

```bash
cd sistema-vendas-java
```

3. **Compile os arquivos fonte:**

```bash
javac -d bin com/sistemavendas/Main.java com/sistemavendas/**/*.java
```

4. **Execute a aplicação:**

```bash
java -cp bin com.sistemavendas.Main
```

🔮 Roteiro de Evolução Futura

[ ] Integração com Banco de Dados Relacional (PostgreSQL/MySQL): Substituição da persistência em arquivo plano (vendas.txt) pelo uso de JDBC/Spring Data JPA.

[ ] Geração Automática de IDs: Transferência da responsabilidade de atribuição de IDs do terminal para a camada de persistência/banco de dados (Auto-Increment / Sequence).

[ ] Interface Web / API RESTful: Exposição das regras de negócio encapsuladas nos serviços (EstoqueService, RelatorioAnaliticoService) através de endpoints HTTP usando Spring Boot.
   
