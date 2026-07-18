
# Dashboard de Gestão de Pedidos

Sistema Full Stack desenvolvido para gerenciamento de pedidos utilizando **Spring Boot** e **Angular**. A aplicação conta com autenticação via JWT, controle de acesso, documentação da API com Swagger, monitoramento utilizando Prometheus e Grafana e uma interface moderna para gerenciamento dos pedidos.

---

# Visão Geral

O sistema permite:

- ✅ Cadastro de usuários
- ✅ Login utilizando JWT
- ✅ Dashboard com gráficos e indicadores
- ✅ Gerenciamento completo de pedidos (CRUD)
- ✅ Documentação completa da API com Swagger
- ✅ Monitoramento da aplicação com Prometheus e Grafana
- ✅ Tratamento global de exceções

---

# Arquitetura

## Stack Tecnológica

### Backend

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- JWT
- Spring Boot Actuator
- Micrometer
- Prometheus
- Swagger / OpenAPI
- Docker

### Frontend

- Angular 17 (Standalone)
- TypeScript
- Angular Material
- Angular Router
- HttpClient
- Guards
- HTTP Interceptors

---

# Arquitetura da Aplicação

## Backend

A aplicação segue uma arquitetura em camadas:

```text
Controller
     ↓
Service
     ↓
Repository
     ↓
Database
```

### Segurança

- Autenticação utilizando JWT
- Spring Security
- Endpoints protegidos
- BCrypt para criptografia de senhas
- Filtro JWT personalizado

### Persistência

- Spring Data JPA
- Hibernate
- PostgreSQL

### Tratamento de Erros

- Global Exception Handler
- Exceções personalizadas

### Observabilidade

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

Métricas monitoradas:

- Requisições HTTP
- Latência da aplicação
- Uso da memória JVM
- Health Check
- Métricas Prometheus

---

## Frontend

O frontend foi desenvolvido utilizando Angular Standalone.

Características:

- Componentes Standalone
- Angular Material
- Dashboard com gráficos
- Guards para proteção de rotas
- HTTP Interceptor para envio automático do JWT
- Comunicação com API utilizando HttpClient

### Rotas da Aplicação

| Rota | Descrição | Protegida |
|------|-----------|-----------|
| `/login` | Tela de Login | ❌ |
| `/register` | Cadastro de Usuário | ❌ |
| `/dashboard` | Dashboard | ✅ |
| `/orders` | Listagem de Pedidos | ✅ |
| `/orders/create` | Cadastro de Pedido | ✅ |

As rotas protegidas exigem autenticação via JWT.

---

# Estrutura do Projeto

```text
Dashboard-de-Gestao-de-Pedidos/

├── BackEnd/
│
│   └── src/main/java/com/claro/ordermanager/
│
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── mapper/
│       ├── repository/
│       ├── security/
│       └── service/
│
├── FrontEnd/
│
│   └── src/app/
│
│       ├── components/
│       │      ├── header/
│       │      └── footer/
│       │
│       ├── guards/
│       ├── interceptors/
│       ├── models/
│       ├── pages/
│       │      ├── dashboard/
│       │      ├── login/
│       │      ├── register/
│       │      ├── order-create/
│       │      └── order-list/
│       │
│       └── services/
│
├── monitoring/
│
└── docker-compose.yml
```

---

# Como Executar

## Utilizando Docker

Na raiz do projeto execute:

```bash
docker compose up --build
```

Após a inicialização:

### Frontend

```
http://localhost:4200
```

### Backend

```
http://localhost:8080
```

### Swagger

```
http://localhost:8080/swagger-ui/index.html
```

### Prometheus

```
http://localhost:9090
```

### Grafana

```
http://localhost:3000
```

---

# Executando Localmente

## Backend

### Pré-requisitos

- Java 17
- Maven
- PostgreSQL

Execute:

```bash
./mvnw clean install
```

Depois:

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```
http://localhost:8080
```

---

## Frontend

Entre na pasta:

```bash
cd FrontEnd
```

Instale as dependências:

```bash
npm install
```

Execute:

```bash
ng serve
```

Acesse:

```
http://localhost:4200
```

---

#  Autenticação

A API utiliza autenticação JWT.

## Registrar Usuário

| Método | Endpoint |
|---------|----------|
| POST | api/users|

### Request

```json
{
  "name": "Administrador",
  "email": "admin@email.com",
  "password": "admin"
}
```

---

## Login

| Método | Endpoint |
|---------|----------|
| POST | /auth/login |

### Request

```json
{
  "email": "admin@email.com",
  "password": "admin"
}
```

### Response

```json
{
  "token": "eyJhbGcKIOhBONIKPIUzI1NiJ9..."
}
```

---

# Gerenciamento de Pedidos

Todos os endpoints exigem autenticação JWT.

```
Authorization: Bearer {token}
```

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| GET | api/pedidos | Lista pedidos |
| GET | api/pedidos/{uuid} | Busca pedido por ID |
| POST | api/pedidos| Cria pedido |
| PATCH | api/pedidos/{uuid} | Atualiza status do pedido |
| DELETE | api/pedidos/{uuid} | Remove pedido |

---

# Observabilidade

A aplicação possui monitoramento integrado utilizando:

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

## Endpoints

| Endpoint | Descrição |
|----------|-----------|
| /actuator/health | Status da aplicação |
| /actuator/info | Informações |
| /actuator/metrics | Métricas |
| /actuator/prometheus | Métricas Prometheus |

---

# Documentação da API

A documentação interativa da API está disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

---

# Segurança

- JWT Authentication
- Spring Security
- BCrypt Password Encoder
- Angular Auth Guard
- HTTP Interceptor
- Endpoints protegidos
- Validação automática do Token

---

#  Funcionalidades

- Login com JWT
- Cadastro de usuários
- CRUD de pedidos
- Dashboard com gráficos
- Swagger/OpenAPI
- PostgreSQL
- Tratamento global de exceções
- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana
- Docker