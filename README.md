# 🛒 Sistema de Vendas em Java

Um sistema em linha de comando (CLI) para gerenciamento de estoque, vendas e relatórios, desenvolvido em Java. 

O projeto passou por uma **refatoração completa**, evoluindo de um modelo procedural simples para uma **arquitetura orientada a objetos moderna**, aplicando princípios do **SOLID**, **Design Patterns** e recursos funcionais do **Java Moderno (Java 17+)**.

---

## 🛠️ Tecnologias e Conceitos Aplicados

- **Linguagem:** Java 17+
- **Paradigma:** Orientação a Objetos + Estilo Funcional (Stream API, Optional e Method References)
- **Design Patterns:**
  - **Strategy Pattern:** Para cálculo dinâmico de políticas de desconto (`DescontoStrategy`).
  - **Repository Pattern:** Para abstração do armazenamento e leitura de relatórios (`RelatorioRepository`).
- **Princípios de Software:**
  - **SOLID:** Princípio da Responsabilidade Única (SRP), Aberto/Fechado (OCP) e Inversão de Dependência (DIP).
  - **Encapsulamento e Imutabilidade:** Uso de Value Objects (`CPF`) com validações de regra de negócio imutáveis.
  - **Coleções Dinâmicas:** Tabela hash (`Map<Integer, Produto>`) no serviço de estoque para buscas performáticas O(1).

---

## 🏛️ Arquitetura do Projeto

O projeto está organizado em pacotes seguindo a separação clara de responsabilidades:

com.sistemavendas/
├── domain/
│   ├── model/         # Entidades de Domínio (Produto, Venda, ItemVenda, CPF)
│   └── strategy/      # Estratégias de Desconto (Strategy Pattern)
├── service/           # Camada de Serviços e Regras de Negócio (EstoqueService)
├── repository/        # Persistência de Dados e Arquivos (RelatorioRepository)
└── Main.java          # Interface CLI (Console) e Orquestrador do Fluxo

---

## 🚀 Funcionalidades

- [x] **Cadastro e Controle de Estoque:** Listagem, busca performática por ID e atualização de quantidades.
- [x] **Realização de Vendas:**
  - Validação estrita de CPF via Value Object.
  - Aplicação automática de regras de desconto por pontos acumulados.
  - Baixa automática no estoque.
- [x] **Relatórios e Histórico:**
  - Consulta aos produtos mais vendidos via **Stream API**.
  - Persistência e leitura do histórico de vendas em arquivo texto (`vendas.txt`).

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
- **Java Development Kit (JDK) 17** ou superior instalado.
- **Git** configurado na máquina.

### Passo a passo

1. **Clone o repositório:**
   git clone https://github.com/mateus-barreto-dev/sistema-vendas-java.git

2. **Navegue até a pasta do projeto:**
   cd sistema-vendas-java

3. **Compile os arquivos Java:**
   javac -d bin com/sistemavendas/Main.java com/sistemavendas/**/*.java

4. **Execute a aplicação:**
   java -cp bin com.sistemavendas.Main

---

## 🔄 Evolução do Projeto (Refatoração)

> **Antes:** Toda a lógica de vendas, validações e leitura de arrays paralelos ficava centralizada em um único arquivo procedural com arrays de tamanho fixo.
>
> **Depois:** Código descentralizado, testável, expansível e seguindo as melhores práticas do ecossistema Java moderno.

---

✍️ Desenvolvido por [Mateus Barreto](https://github.com/mateus-barreto-dev).