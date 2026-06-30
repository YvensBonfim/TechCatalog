
---

# # TechCatalog API

O **TechCatalog** é uma API REST robusta desenvolvida para o gerenciamento de um catálogo de produtos tecnológicos e controle de usuários. O sistema conta com um ecossistema completo de segurança baseado em controle de acesso por perfis (RBAC), recuperação de senhas via e-mail, tratamento centralizado de exceções e uma cobertura rigorosa de testes automatizados organizados por camadas.

---

## ## 🛠️ Tecnologias Utilizadas

* **Framework Principal:** Spring Boot 3.5.14
* **Linguagem:** Java 17
* **Acesso a Dados:** Spring Data JPA / Hibernate
* **Bancos de Dados:** PostgreSQL (Produção/Dev) & H2 Database (Testes/Memória)
* **Segurança:** Spring Security, OAuth2 Resource Server & Authorization Server (JWT)
* **Gerenciamento de Variáveis:** Java Dotenv (`io.github.cdimascio:java-dotenv`)
* **Ferramenta de Build:** Maven

---

## ## 🔐 Segurança & Perfis de Acesso

A API utiliza **OAuth2 Resource Server** com tokens **JWT** para autenticação. Os escopos e permissões são extraídos diretamente do *claim* `authorities` do token.

### Perfis de Usuário (Roles)

* `ROLE_ADMIN`: Acesso total ao sistema, incluindo gerenciamento de usuários.
* `ROLE_OPERATOR`: Permissão para modificar o catálogo (produtos e categorias).

### Matriz de Permissões das Rotas

| Endpoint | Verbo | Descrição | Acesso |
| --- | --- | --- | --- |
| `/auth/**` | `POST/PUT` | Recuperação de senha e geração de tokens | **Público** |
| `/categories/**` | `GET` | Listagem e consulta de categorias | **Público** |
| `/categories/**` | `POST/PUT/DELETE` | Cadastro, edição e deleção de categorias | `ADMIN`, `OPERATOR` |
| `/products/**` | `GET` | Consulta paginada e busca de produtos | **Público** |
| `/products/**` | `POST/PUT/DELETE` | Cadastro, edição e deleção de produtos | `ADMIN`, `OPERATOR` |
| `/users/profile` | `GET` | Retorna o perfil do usuário logado | `ADMIN`, `OPERATOR` |
| `/users/**` | `GET/PUT/DELETE` | Gerenciamento de usuários do sistema | `ADMIN` |
| `/users` | `POST` | Autocadastro de novos usuários | **Público** |

---

## ## 🚫 Tratamento de Erros (Custom Exceptions)

O projeto implementa um manipulador global de exceções (`@ControllerAdvice`) que padroniza as respostas de erro da API utilizando a seguinte estrutura JSON:

### Erros Padrão (`StandardError`)

Retornado em caso de recursos não encontrados (`404`) ou falhas de persistência (`400`).

```json
{
  "timestamp": "2026-06-30T15:30:00Z",
  "status": 404,
  "error": "Resource Not Found",
  "message": "Entity not found",
  "path": "/products/99"
}

```

### Erros de Validação (`ValidationError`)

Retornado quando campos enviados na requisição falham nas regras do Bean Validation (`422 Unprocessable Entity`). Retorna uma lista detalhada com cada campo inválido:

```json
{
  "timestamp": "2026-06-30T15:32:00Z",
  "status": 422,
  "error": "Validation exception",
  "message": "Validation failed for object...",
  "path": "/products",
  "errors": [
    {
      "fieldName": "name",
      "message": "O nome do produto não pode ser vazio"
    }
  ]
}

```

---

## 🗄️ Banco de Dados & Seed Inicial

O projeto já possui um script de semente (import.sql ou seed.sql) pré-configurado que popula a base de dados automaticamente em ambientes de teste e desenvolvimento.

    Categorias Iniciais: Livros, Eletrônicos e Computadores.

    Carga de Dados: 25 produtos cadastrados (incluindo o Macbook Pro, Smart TV e uma ampla variedade de PCs Gamer) mapeados em suas respectivas categorias através de uma tabela associativa (tb_product_category).

    Usuários Padrão:

        alex@gmail.com (Senha: 123456) -> ROLE_OPERATOR

        maria@gmail.com (Senha: 123456) -> ROLE_OPERATOR, ROLE_ADMIN

    ⚠️ Importante para testes de e-mail: Para utilizar e validar o fluxo de recuperação de senha (/auth/recover-token), altere o terceiro usuário do seed ou insira um novo registro diretamente no banco contendo um endereço de e-mail real e válido que você possa acessar pelo seu client SMTP (como o Mailtrap).

---

## ## 🧪 Estrutura de Testes Automatizados

A aplicação adota uma estratégia de testes piramidal bem definida, cobrindo validações de ponta a ponta à persistência isolada:

### 1. Testes de Repositório (Data Access)

* **Foco:** Validação de persistência, chaves primárias autoincrementais, buscas (`findById`) e exclusões de registros.
* **Ferramenta:** `@DataJpaTest` para carregar apenas os componentes de dados usando o banco em memória.

### 2. Testes de Serviço (Camada de Negócio)

* **Testes Unitários Isolados (`ProductServiceTest`):** Utiliza Mockito (`@ExtendWith(SpringExtension.class)`) e mocks (`@Mock`, `@InjectMocks`) para simular os repositórios, garantindo comportamento correto do fluxo lógico e lançamento de exceções customizadas (`ResourceNotFoundException`, `DataBaseException`) sem tocar no banco de dados.
* **Testes de Integração (`ProductServiceIntegration`):** Utiliza `@SpringBootTest` junto com `@Transactional` para testar os serviços integrados ao banco de dados real (ou banco de testes), validando paginações reais e integridade referencial.

### 3. Testes de Controladores (Camada Web / Recursos)

* **Testes de Unidade da Web (`ProductResourceTest`):** Valida os contratos HTTP isoladamente utilizando `@WebMvcTest` e moca o serviço com `@MockitoBean`. A segurança é desativada localmente nesta classe para focar exclusivamente na serialização JSON e códigos de status HTTP (`200 OK`, `204 No Content`, `404 Not Found`).
* **Testes de Integração Web Completo (`ProductResourceIT`):** Testes de integração de ponta a ponta via `@AutoConfigureMockMvc`. Utiliza uma classe utilitária customizada (`TokenUtil`) para efetuar chamadas autenticadas na rota `/oauth2/token` via OAuth2 Password Grant, injetando o cabeçalho `Authorization: Bearer <token>` nas requisições protegidas.




### Como rodar a suite de testes:

```bash
./mvnw test

```

---

## ## ⚙️ Como Executar o Projeto

### Pré-requisitos

* Java JDK 17 instalado
* Maven 3.x instalado ou uso do `./mvnw` incluso
* Banco de dados PostgreSQL (opcional, configurado via `.env`)

### 1. Configuração das Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto para gerenciar as credenciais com segurança (suportado pela biblioteca `java-dotenv` configurada no `pom.xml`):

```env
DB_URL=jdbc:postgresql://localhost:5432/techcatalog
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

SECURITY_CLIENT_ID=seu-client-id
SECURITY_CLIENT_SECRET=seu-client-secret

EMAIL_HOST=smtp.mailtrap.io
EMAIL_PORT=2525
EMAIL_USERNAME=seu_usuario_smtp
EMAIL_PASSWORD=sua_senha_smtp

```

### 2. Compilar e Rodar a Aplicação

Execute os seguintes comandos no terminal:

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/techcatalog.git

# Entrar na pasta do projeto
cd techcatalog

# Compilar o projeto e baixar as dependências
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run

```

A API estará disponível em `http://localhost:8080`.
O Console do banco de dados H2 poderá ser acessado em `http://localhost:8080/h2-console` (durante o desenvolvimento).

---

## ## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](https://www.google.com/search?q=LICENSE) para mais detalhes.

---

Feito com 💻 por [Yvens Bonfim](https://github.com/YvensBonfim).