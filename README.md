# Volta API

API REST do **Volta**, plataforma de gestão de resíduos que conecta empresas geradoras a cooperativas de reciclagem. A API cobre o registro de incidentes com resíduos nas áreas da empresa, a solicitação e o acompanhamento de coletas, a avaliação das cooperativas e os indicadores ambientais (ESG).

As regras de negócio seguem a legislação ambiental brasileira: Política Nacional de Resíduos Sólidos (Lei 12.305/2010), Resolução CONAMA 275/2001 e ABNT NBR 10004.

---

## Sumário

- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Estrutura de pastas](#estrutura-de-pastas)
- [Pré-requisitos](#pré-requisitos)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Como executar](#como-executar)
- [Documentação da API (Swagger)](#documentação-da-api-swagger)
- [Autenticação e perfis de acesso](#autenticação-e-perfis-de-acesso)
- [Regras de negócio](#regras-de-negócio)
- [Banco de dados](#banco-de-dados)
- [Tratamento de erros](#tratamento-de-erros)
- [CI/CD](#cicd)
- [Licença](#licença)

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem |
| Spring Boot | 4.0.7 | Framework base |
| Spring Web MVC | — | API REST |
| Spring Data JPA / Hibernate | — | Persistência |
| Spring Security + JJWT | 0.12.6 | Autenticação stateless com JWT |
| Bean Validation | — | Validação dos dados de entrada |
| PostgreSQL | 16 | Banco de dados (Neon em nuvem) |
| Flyway | — | Versionamento do banco |
| springdoc-openapi | 3.0.2 | Documentação Swagger / OpenAPI |
| Spring Boot Actuator | — | Health check |
| Lombok | — | Redução de código repetitivo |
| Docker | — | Empacotamento da aplicação |
| GitHub Actions | — | Integração e entrega contínuas |

---

## Arquitetura

A aplicação segue uma arquitetura em camadas. Cada requisição percorre o fluxo abaixo:

```
Controller  →  UseCase (interface)  →  Service  →  Repository / Procedure / Function  →  PostgreSQL
    │                                     │
    └── DTO de request/response           └── Mapper (DTO ⇄ entidade)
```

| Camada | Responsabilidade |
|---|---|
| **Controller** | Recebe a requisição HTTP, valida o DTO (`@Valid`), checa o perfil (`@PreAuthorize`) e devolve a resposta |
| **Controller Docs** | Interfaces com as anotações do Swagger, para manter os controllers limpos |
| **UseCase** | Contrato (interface) dos casos de uso que o controller consome |
| **Service** | Implementa os casos de uso e aplica as regras de negócio |
| **Mapper** | Converte entre DTOs e entidades |
| **Repository** | Acesso a dados via Spring Data JPA |
| **Procedure / Function** | Chamadas às procedures e functions PL/pgSQL do banco |
| **Enums** | Valores de domínio e regras associadas a eles (transições de status, prazos, categorias de resíduo) |
| **Validation** | Validações customizadas (CNPJ, categorias de resíduo) |
| **Handler** | Tratamento global de exceções no formato `ProblemDetail` |

---

## Estrutura de pastas

```
src/main/java/com/volta/api
├── config/              # Segurança, Swagger, propriedades e bootstrap do administrador
├── controller/          # Endpoints REST
│   └── docs/            # Interfaces de documentação do Swagger
├── database/
│   ├── entity/          # Entidades JPA
│   ├── repository/      # Repositórios Spring Data
│   ├── procedure/       # Chamadas às procedures do banco
│   └── function/        # Chamadas às functions do banco
├── dto/
│   ├── request/         # Dados de entrada (inclui request/update)
│   └── response/        # Dados de saída
├── enums/               # Enums de domínio e regras associadas
├── exception/           # Exceções de negócio
├── handler/             # Tratamento global de erros e de autenticação
├── mapper/              # Conversão DTO ⇄ entidade
├── security/            # Filtro JWT, geração de token e usuário autenticado
├── service/             # Regras de negócio
├── usecase/             # Interfaces dos casos de uso
└── validation/          # Validadores customizados (CNPJ, categorias)

src/main/resources
├── application.yaml
└── db/migration/        # Scripts Flyway (V1 a V6)
```

---

## Pré-requisitos

- **JDK 21**
- **Maven 3.9+**
- **PostgreSQL 16**: o projeto usa o Neon em nuvem, mas um Postgres local também funciona (veja [Como executar](#como-executar))
- **Docker** (opcional)

---

## Variáveis de ambiente

| Variável | Obrigatória | Descrição | Exemplo |
|---|---|---|---|
| `DATABASE_HOST` | Sim | Host do banco usado na URL de conexão | `ep-xxx.sa-east-1.aws.neon.tech` |
| `DATABASE_USERNAME` | Sim | Usuário do banco | `volta` |
| `DATABASE_PASSWORD` | Sim | Senha do banco | — |
| `JWT_KEY` | Sim | Chave de assinatura do JWT (mínimo de 32 bytes) | — |
| `JWT_EXPIRATION` | Sim | Validade do token, em milissegundos | `86400000` (24 h) |
| `SWAGGER_USERNAME` | Sim | Usuário de acesso à documentação Swagger | `dev` |
| `SWAGGER_PASSWORD` | Sim | Senha de acesso à documentação Swagger | — |
| `ADMIN_EMAIL` | Não | E-mail do administrador criado na inicialização | `admin@volta.com` |
| `ADMIN_PASSWORD` | Não | Senha do administrador criado na inicialização | — |
| `ADMIN_COMPANY_ID` | Não | ID da empresa do administrador | UUID |
| `SPRING_DATASOURCE_URL` | Não | Sobrescreve a URL completa do banco (útil para Postgres local) | `jdbc:postgresql://localhost:5432/volta` |

> Por padrão, a URL do banco aponta para o Neon com SSL obrigatório: `jdbc:postgresql://${DATABASE_HOST}/neondb?sslmode=require&channel_binding=require`. Para usar um Postgres local sem SSL, defina `SPRING_DATASOURCE_URL`.

### Administrador inicial

Na inicialização, a classe `AdminBootstrap` cria um usuário com perfil **ADMIN** quando `ADMIN_EMAIL`, `ADMIN_PASSWORD` e `ADMIN_COMPANY_ID` estão preenchidos e ainda não existe usuário com esse e-mail. A empresa informada em `ADMIN_COMPANY_ID` precisa existir no banco.

---

## Como executar

### Com Maven

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. As migrations do Flyway rodam automaticamente na inicialização.

### Com Postgres local via Docker

```bash
docker run -d --name volta-db -e POSTGRES_DB=volta -e POSTGRES_USER=volta -e POSTGRES_PASSWORD=volta -p 5432:5432 postgres:16
```

Depois, defina `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/volta`, `DATABASE_USERNAME=volta` e `DATABASE_PASSWORD=volta`.

### Com Docker

```bash
docker build -t volta-api .
```

```bash
docker run -p 8080:8080 --env-file .env volta-api
```

O `Dockerfile` usa build em dois estágios: compila com Maven e roda o `.jar` em uma imagem JRE Alpine, com usuário sem privilégios.

### Testes

```bash
mvn verify
```

### Health check

```
GET /actuator/health
```

---

## Documentação da API (Swagger)

A documentação é gerada automaticamente pelo springdoc-openapi e é a referência oficial dos endpoints: rotas, perfis de acesso, parâmetros, corpos de requisição, respostas e códigos de erro, em português.

| Recurso | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI (JSON) | `http://localhost:8080/api-docs` |

A documentação é **protegida por usuário e senha** (HTTP Basic), definidos em `SWAGGER_USERNAME` e `SWAGGER_PASSWORD`. Ao abrir o Swagger UI, o navegador pede essas credenciais.

**Para testar os endpoints pelo Swagger:**

1. Abra o Swagger UI e informe o usuário e a senha da documentação.
2. Em **Autenticação**, execute `POST /auth/login` e copie o `token` retornado.
3. Clique em **Authorize**, cole o token e confirme.
4. Os endpoints protegidos passam a enviar `Authorization: Bearer <token>`.

---

## Autenticação e perfis de acesso

A autenticação é **stateless** com JWT. O token é obtido em `POST /auth/login` e deve ser enviado em todas as requisições protegidas:

```
Authorization: Bearer <token>
```

O token carrega o ID do usuário, a empresa e o perfil. Todas as consultas são restritas aos dados da **empresa do usuário autenticado**.

| Perfil | Descrição |
|---|---|
| `ADMIN` | Administra a plataforma: empresas, cooperativas, áreas, usuários e tipos de resíduo |
| `MANAGER` | Gestor da empresa: coletas, encerramento de incidentes, avaliações e ESG |
| `EMPLOYEE` | Funcionário: registra incidentes e consulta as áreas da própria empresa |
| `OPERATOR` | Reservado para uso futuro |

---

## Regras de negócio

### 1. Ciclo de vida da coleta

A coleta segue uma máquina de estados. Transições fora do fluxo são recusadas.

```
REQUESTED ──► SCHEDULED ──► IN_PROGRESS ──► COMPLETED
    │             │  ▲
    │             └──┘ (reagendamento)
    ▼             ▼
 CANCELED ◄───────┘
```

- `COMPLETED` e `CANCELED` são estados finais.
- Uma coleta só pode ser cancelada antes de começar, e o cancelamento exige observação.
- O agendamento é feito pelo endpoint `/schedule`, não pelo `/status`.

**Base:** rastreabilidade do resíduo prevista na PNRS (Lei 12.305/2010).

### 2. Responsabilidade do gerador até o destino final

- Não é possível solicitar coleta para incidente encerrado.
- Um incidente só pode ter uma coleta ativa por vez.
- Um incidente com coleta em andamento não pode ser encerrado manualmente.
- Incidente com resíduo perigoso só pode ser encerrado depois de uma coleta concluída.
- Concluir a coleta encerra o incidente automaticamente e notifica o funcionário que o registrou.

**Base:** PNRS, art. 27, §1º. A contratação de terceiros não isenta o gerador da responsabilidade pelo resíduo.

### 3. Classificação do resíduo e compatibilidade com a cooperativa

Os tipos de resíduo usam as categorias da coleta seletiva da **CONAMA 275/2001**:

| Categoria | Cor | Perigosa |
|---|---|---|
| `PAPEL` | Azul | Não |
| `PLASTICO` | Vermelho | Não |
| `VIDRO` | Verde | Não |
| `METAL` | Amarelo | Não |
| `MADEIRA` | Preto | Não |
| `PERIGOSO` | Laranja | Sim |
| `SAUDE` | Branco | Sim |
| `RADIOATIVO` | Roxo | Sim |
| `ORGANICO` | Marrom | Não |
| `NAO_RECICLAVEL` | Cinza | Não |

- Resíduo com risco `HIGH` é tratado como **Classe I (perigoso)** da NBR 10004; os demais como Classe II.
- As categorias perigosas exigem nível de risco `HIGH`.
- A cooperativa só recebe a coleta se a categoria do resíduo estiver nas suas especialidades.
- Resíduo perigoso exige cooperativa com a especialidade `PERIGOSO`.
- Resíduo `RADIOATIVO` não pode ser destinado a cooperativas, pois é atribuição de empresa autorizada pela CNEN.

**Base:** CONAMA 275/2001, ABNT NBR 10004 e PNRS, arts. 37 e 38.

### 4. Prioridade e prazo de agendamento

- Se o nível de contaminação não for informado, assume-se o risco padrão do tipo de resíduo.
- Resíduo perigoso ou contaminação `HIGH` elevam a prioridade do incidente para no mínimo `HIGH`.
- Incidente `CRITICAL` gera coleta urgente.
- A coleta deve ser agendada dentro do prazo da prioridade, contado a partir da solicitação:

| Prioridade | Prazo máximo para agendamento |
|---|---|
| Urgente / `CRITICAL` | 24 horas |
| `HIGH` | 72 horas |
| `MEDIUM` | 7 dias |
| `LOW` | 15 dias |

Se o prazo já tiver vencido, a coleta deve ser agendada em até 24 horas.

**Base:** política interna da plataforma.

### 5. Avaliação das cooperativas

- Só coletas concluídas podem ser avaliadas, e cada coleta uma única vez.
- A nota média da cooperativa é recalculada a cada nova avaliação.

### 6. Validação de CNPJ

- Os dígitos verificadores do CNPJ de empresas e cooperativas são validados.
- O CNPJ pode ser enviado com ou sem máscara, e o formato **alfanumérico** também é aceito.

**Base:** algoritmo de dígitos verificadores da Receita Federal e IN RFB 2.229/2024.

### 7. Métricas ESG

- O total reciclado não pode ser maior que o total de resíduos.
- Só é permitida uma métrica por empresa em cada período (`YYYY-MM`).
- O score ESG considera o percentual de reciclagem mais recente da empresa.

---

## Banco de dados

O schema é versionado com **Flyway** em `src/main/resources/db/migration`:

| Migration | Conteúdo |
|---|---|
| `V1__create_tables.sql` | Tabelas, chaves estrangeiras e constraints |
| `V2__seed_roles.sql` | Perfis de acesso (`ADMIN`, `EMPLOYEE`, `OPERATOR`, `MANAGER`) |
| `V3__create_procedure.sql` | Procedures de negócio |
| `V4__create_functions.sql` | Functions de cálculo |
| `V5__create_triggers.sql` | Tabela e triggers de auditoria |
| `V6__create_indexes.sql` | Índices de desempenho |

### Procedures

| Procedure | Descrição |
|---|---|
| `update_collection_status` | Atualiza o status da coleta e registra a mudança no histórico |
| `schedule_collection` | Agenda a coleta, muda o status para `SCHEDULED` e registra no histórico |
| `close_incident` | Encerra o incidente e cria uma notificação para o funcionário |

### Functions

| Function | Descrição |
|---|---|
| `calculate_recycling_percentage` | Calcula o percentual reciclado |
| `calculate_collection_completion_hours` | Calcula as horas entre a solicitação e a conclusão da coleta |
| `calculate_company_esg_score` | Retorna o score ESG da empresa |

### Auditoria

A tabela `audit_log` registra automaticamente, via triggers, todas as inserções, alterações e exclusões nas tabelas `incident`, `collection` e `esg_metric`, guardando o estado anterior e o novo em JSONB.

---

## Tratamento de erros

Todos os erros seguem o formato **ProblemDetail** (RFC 9457):

```json
{
  "type": "about:blank",
  "title": "Unprocessable Content",
  "status": 422,
  "detail": "Invalid status transition from REQUESTED to COMPLETED",
  "instance": "/collections/3fa85f64-5717-4562-b3fc-2c963f66afa6/status"
}
```

Erros de validação (400) incluem o campo `errors`, com a mensagem de cada campo:

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation error",
  "errors": {
    "cnpj": "CNPJ inválido"
  }
}
```

| Código | Quando ocorre |
|---|---|
| `400` | Dados inválidos |
| `401` | Token ausente, inválido ou expirado, ou credenciais incorretas |
| `403` | Usuário sem permissão para o recurso |
| `404` | Recurso não encontrado |
| `409` | Conflito com dados existentes (CNPJ ou e-mail duplicado, coleta ativa) |
| `422` | Violação de regra de negócio |
| `500` | Erro interno |

---

## CI/CD

Os pipelines ficam em `.github/workflows`:

| Workflow | Gatilho | Etapas |
|---|---|---|
| **CI** (`ci.yaml`) | Pull request e push em `develop` e `main` | Valida o PR, sobe um PostgreSQL 16 de teste, roda `mvn verify` e publica o relatório de testes |
| **CD** (`cd.yaml`) | Push em `develop` (QA) e `main` (produção), ou execução manual | Build e testes, depois gera e publica a imagem Docker pelo workflow reutilizável do `volta-devops` |

---

## Licença

Distribuído sob a licença MIT. Veja o arquivo [LICENSE](LICENSE).
