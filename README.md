# Money Flow

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

## Sumário

- [Descrição](#descricao)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Instruções de uso](#instruções-de-uso)
- [API Endpoints](#api-endpoints)
- [Autenticação](#autenticação)
- [Banco de Dados](#banco-de-dados)
- [Contribuição](#contribuições)

## Descrição 
Money Flow é uma aplicação Backend para gestão financeira pessoal ou familiar.

O projeto foi construído com a linguagem **Java**, framework **Spring**, banco de dados **PostgresSQL**, **Spring Security** e **JWT** para controle de autenticação.

## Pré-requisitos
* Java 21
* Bande dados PostgreSQL 16.9
* Spring Boot 3.4.4

## Instalação

1. Clonar o repositório:

```bash
git clone https://github.com/HigorTadeu/java-money-flow.git
```

2. Instalar dependência utilizando o Maven

3. Instalar o banco de dados [PostgresSQL](https://www.postgresql.org/)

4. Criar as variáveis de ambiente

### Variáveis de Ambiente
Entradas para Banco de Dados: **DB_HOST, DB_PORT, DB_BASE, DB_USER, DB_PASS**

Entrada para ambiente: **APP_PROFILE** com as opções (dev, hom, prod) representando cada ambeinte da aplicação

Entrada para criação do Token: **JWT_SECRET**

## Instruções de Uso
1. Executar aplicação com o Maven
2. A API é acessada através http://localhost:8080

## API Endpoints
A API fornece os seguintes endpoints:

```markdown
GET /transactions - Recupera uma lista de todas as transações. (todos os usuários autenticados).

GET /transactions/{id} - Recupera uma transação com base no ID. (todos os usuários autenticados).

POST /transactions - Cadastra uma nova transação. (todos os usuários autenticados) (todos os usuários autenticados).

PUT /transactions/{id} - Atualiza uma transação. (todos os usuários autenticados) (todos os usuários autenticados).

DELETE /transactions/{id} - Exclui uma transação. (todos os usuários autenticados).

POST /auth/login - Realiza login no App
```

## Autenticação
A API utiliza o Spring Security para controle de autenticação. As seguintes permissões (roles) estão disponíveis:
```bash
USER -> Papel padrão para usuários autenticados.
ADMIN -> Papel de administrador para gerenciamento de parceiros (cadastro de novos parceiros).
```
Endpoint para login /auth/login
* Usuário inicial com permissões **ADMIN** é usuário **admin** senha **Admin#321**

Com o Token retornado deve acessar os endpoints desejados.

## Banco de Dados
* PostgreSQL
* Base: money_flow
* Usuário: postgres
* Senha: postgres
* Encoding: UTF8

DDL:
```sql
CREATE DATABASE money_flow
WITH
OWNER = postgres
ENCODING = 'UTF8'
LOCALE_PROVIDER = 'libc'
CONNECTION LIMIT = -1
IS_TEMPLATE = False;
```
## Contribuições
Contribuições são bem-vindas!
Se você encontrar algum problema ou tiver sugestões de melhorias, abra uma issue ou envie um pull request para o repositório.

Ao contribuir com este projeto, por favor, siga o estilo de código existente, as convenções de commit e envie suas alterações em uma branch separada.

---

---
## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database](#database)
- [Contributing](#contributing)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/HigorTadeu/java-money-flow.git
```

2. Install dependencies with Maven

3. Install [PostgresSQL](https://www.postgresql.org/)

4. Create enviriment variables

### Environment Variables
Database Inputs: **DB_HOST, DB_PORT, DB_BASE, DB_USER, DB_PASS**

Environment Input: **APP_PROFILE** with options (dev, hom, prod) representing each application environment

Token Creation Input: **JWT_SECRET**

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080


## API Endpoints
The API provides the following endpoints:

```markdown
GET /transactions - Retrieve a list of all transactions. (all authenticated users).

GET /transactions/{id} - Retrieve a transaction based ID. (all authenticated users).

POST /transactions - Register a new transaction (all authenticated users).(ADMIN access required).

PUT /transactions/{id} - Update a transaction (all authenticated users).(ADMIN access required).

DELETE /transactions/{id} - Delete a transaction (all authenticated users).

POST /auth/login - Login into the App
```

## Authentication
The API uses Spring Security for authentication control. The following roles are available:

```
USER -> Standard user role for logged-in users.
ADMIN -> Admin role for managing partners (registering new partners).
```
Login endpoint: /auth/login
* The initial user with ADMIN permissions is username admin, password Admin#321.

Use the returned token to access the desired endpoints.

## Database
The project utilizes [PostgresSQL](https://www.postgresql.org/) as the database. The necessary database migrations are managed using Flyway.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.
