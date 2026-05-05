<div align="center">

# 📋 TaskManager

**Sistema de gerenciamento de tarefas pessoais com autenticação JWT**

[![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

</div>

---

## 📖 Sobre o Projeto

O **TaskManager** é uma aplicação web full-stack para gerenciamento de tarefas pessoais. O sistema permite que usuários se cadastrem, façam login e gerenciem suas tarefas com controle de prioridade e status, tudo protegido por autenticação via **JWT (JSON Web Token)**.

---

## ✨ Funcionalidades

| Módulo | Funcionalidade | Status |
|--------|---------------|---|
| 👤 Usuários | Registro de conta | ✅ Concluído |
| 👤 Usuários | Login com JWT |  ✅ Concluído |
| 👤 Usuários | Dashboard |  ✅ Concluído |
| ✅ Tarefas | Criar tarefa |  ✅ Concluído |
| ✅ Tarefas | Listar tarefas |  ✅ Concluído |
| ✅ Tarefas | Editar tarefa |  ✅ Concluído |
| ✅ Tarefas | Deletar tarefa |  ✅ Concluído |

---

## 🛠️ Tecnologias Utilizadas

### Back-End
- **[Spring Boot 4.0.6](https://spring.io/projects/spring-boot)** — Framework principal
- **[Spring Security](https://spring.io/projects/spring-security)** — Autenticação e autorização
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)** — Persistência de dados
- **[JJWT 0.12.5](https://github.com/jwtk/jjwt)** — Geração e validação de tokens JWT
- **[PostgreSQL](https://www.postgresql.org/)** — Banco de dados relacional
- **[Lombok](https://projectlombok.org/)** — Redução de boilerplate
- **[Jakarta Validation](https://beanvalidation.org/)** — Validação de dados de entrada

### Front-End
- **HTML5 / CSS3**
- **[Bootstrap](https://getbootstrap.com/)** — Estilização responsiva
- **JavaScript** — Consumo da API REST

### DevOps
- **[Docker Compose](https://docs.docker.com/compose/)** — Banco de dados em container
- **[GitHub Actions](https://github.com/features/actions)** — CI/CD automatizado
- **[Apache Maven](https://maven.apache.org/)** — Build e gerenciamento de dependências

---

## 📁 Estrutura do Projeto

```
src/main/
├── java/com/taskmanager/
│   ├── ApplicationTaskManager.java       # Entry point
│   ├── configs/
│   │   ├── SecurityConfig.java           # Configuração do Spring Security
│   │   ├── JwtAuthFilter.java            # Filtro de autenticação JWT
│   │   └── PageController.java           # Rotas das páginas estáticas
│   ├── features/
│   │   ├── tasks/
│   │   │   ├── TaskController.java       # Endpoints REST de tarefas
│   │   │   ├── TaskService.java          # Regras de negócio
│   │   │   ├── interfaces/
│   │   │   │   └── TaskRepository.java   # Queries do banco
│   │   │   └── repository/
│   │   │       └── Task.java             # Entidade de tarefa
│   │   └── users/
│   │       ├── UserController.java       # Endpoints REST de usuários
│   │       ├── UserService.java          # Lógica de autenticação
│   │       ├── interfaces/
│   │       │   └── UserRepository.java
│   │       └── repository/
│   │           └── User.java             # Entidade de usuário
│   ├── handlers/
│   │   ├── GlobalExceptionHandler.java   # Tratamento global de erros
│   │   └── UnauthorizedExceptionHandler.java
│   └── utils/
│       ├── JwtUtils.java                 # Geração de tokens
│       └── ApiResponse.java              # Wrapper de resposta padrão
└── resources/
    ├── application.properties            # Configurações da aplicação
    └── public/                           # Assets estáticos (HTML, CSS, JS)
```

---

## 🔌 Endpoints da API

### 🔓 Autenticação (Públicos)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/auth/register` | Registrar novo usuário |
| `POST` | `/auth/login` | Login e obtenção do JWT |

### 🔐 Tarefas (Requer JWT)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/tasks/list` | Listar tarefas do usuário autenticado |
| `POST` | `/tasks/create` | Criar nova tarefa |
| `PUT` | `/tasks/edit/{taskId}` | Editar tarefa por ID |
| `DELETE` | `/tasks/delete/{taskId}` | Deletar tarefa por ID |

### 📄 Páginas (Públicas)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/` | Redireciona para `/login` |
| `GET` | `/login` | Página de login |
| `GET` | `/register` | Página de cadastro |
| `GET` | `/dashboard` | Dashboard principal |

---

### 📦 Formato de Resposta

Todos os endpoints retornam um wrapper padrão:

```json
{
  "success": true,
  "message": "Operação realizada com sucesso",
  "data": { ... },
  "timestamp": "2026-05-05T14:30:00"
}
```

### 📝 Exemplo — Criar Tarefa

**Request**
```http
POST /tasks/create
Authorization: Bearer <seu_token_jwt>
Content-Type: application/json

{
  "title": "Estudar Spring Security",
  "description": "Revisar filtros JWT e configurações de CSRF",
  "priority": "HIGH",
  "status": "PENDING"
}
```

**Response** `201 Created`
```json
{
  "success": true,
  "message": "Tarefa criada com sucesso",
  "data": {
    "id": 1,
    "title": "Estudar Spring Security",
    "description": "Revisar filtros JWT e configurações de CSRF",
    "priority": "HIGH",
    "status": "PENDING",
    "createdAt": "2026-05-05T14:30:00",
    "updatedAt": "2026-05-05T14:30:00"
  },
  "timestamp": "2026-05-05T14:30:00"
}
```

---

## 🗃️ Modelo de Dados

### User
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | `Long` | Identificador único |
| `email` | `String` | Email (único) |
| `password` | `String` | Senha criptografada com BCrypt |

### Task
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | `Long` | Identificador único |
| `user` | `User` | Dono da tarefa (FK) |
| `title` | `String` | Título (3–255 caracteres) |
| `description` | `String` | Descrição (3–255 caracteres) |
| `priority` | `Enum` | `LOW` / `MEDIUM` / `HIGH` |
| `status` | `Enum` | `PENDING` / `IN_PROGRESS` / `DONE` |
| `createdAt` | `LocalDateTime` | Data de criação (automático) |
| `updatedAt` | `LocalDateTime` | Data de atualização (automático) |

---

## ⚙️ Como Rodar Localmente

### Pré-requisitos

- Java 25+
- Maven 3.9+
- Docker e Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/edudevvv/crud-todolist-faculdade.git
cd crud-todolist-faculdade
```

### 2. Suba a aplicação com Docker

```bash
docker-compose down && docker-compose up --build -d
```
--- 
### 🔗 Links úteis:
```
Aplicação -> http://localhost:3000

Documentação Swagger -> http://localhost:3000/docs
```


---


## 🚀 CI/CD

O projeto utiliza **GitHub Actions** para build automatizado a cada push ou Pull Request na branch `main`:

```yaml
Trigger: push/PR → main
  └── Setup JDK 25 (Temurin)
  └── Cache Maven dependencies
  └── mvn -B package
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">
  Desenvolvido por <a href="https://github.com/edudevvv"><strong>edudevvv</strong></a>
</div>
