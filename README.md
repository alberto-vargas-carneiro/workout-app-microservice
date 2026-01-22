# Workout App

Este projeto contém todo o ecossistema da aplicação Workout App, incluindo:

- **Frontend (Next.js)**
- **User Service (Java Spring Boot)**
- **Exercise Service (Java Spring Boot)**
- **Workout Service (Java Spring Boot)**
- **PostgreSQL**
- **pgAdmin**

Tudo é orquestrado via **Docker Compose**, permitindo subir todo o ambiente com um único comando.

---

## 📦 Requisitos

Antes de rodar o projeto, instale:

- Docker desktop

---

## ▶️ Como Executar

No diretório raiz, execute:

```bash
docker compose up --build
```
---

## 🔑 Credenciais
### PostgreSQL
Host: postgres
Port: 5432
User: postgres
Password: postgres
Database: workout

### pgAdmin
Email: admin@admin.com
Senha: admin

#### Conectar o PostgreSQL no pgAdmin

Após logar, adicione um novo servidor:

| Campo    | Valor      |
|----------|------------|
| Name     | workout-db |
| Host     | postgres   |
| Port     | 5432       |
| User     | postgres   |
| Password | postgres   |

---

## 🛑 Como Parar Tudo

Para parar todos os containers:

docker compose down


Para parar e remover volumes (isso apaga o banco):

docker compose down -v
