# Money Flow

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

Money Flow é uma aplicação Backend simples para lançamento de transações finaceiras pessoais ou da família.

O projeto foi construído em **Java, Java Spring, banco de dados PostgresSQL, Spring Security e JWT para controle de autenticação**.

Aplicação feita para estudo e aprimorar conhecimentos além de atender uma necessidade pessoal não usando mais planilihas.

# Definições Técnicas
## Instruções de Instalação

### Pré-requisitos
* Java 21
* Bande dados PostgreSQL - 

## Banco de Dados
* PostgreSQL
* Base: money_flow
* Usuário: postgres
* Senha: Acesso123
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

## Variáveis de Ambiente
Deve ser configurada as variáveis de ambiente para que a aplicação fucnione corretamente

Entradas para Banco de Dados: DB_HOST, DB_PORT, DB_BASE, DB_USER, DB_PASS

Entrada para ambiente: APP_PROFILE com as opções (dev, hom, prod) representando cada ambeinte da aplicação

Entrada de palavra usada para criar Token: JWT_SECRET

### Instruções de Uso
1. Rodar aplicação 
2. Efetuar login através do endpoint /auth/login
   * O usuário inicial com permissões ADMIN é usuário **admin** senha **Admin#321**
3. Com o toquem retornado deverá chamar os endpoints desejados

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

## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080


## API Endpoints
The API provides the following endpoints:

```markdown
GET /transactions - Retrieve a list of all transactions. (all authenticated users).

GET /transactions/{id} - Retrieve a transaction based ID. (all authenticated users).

POST /transactions - Register a new transaction (all authenticated users).(ADMIN access required).

PUT /transactions - Update a transaction (all authenticated users).(ADMIN access required).

DELETE /transactions - Delete a transaction (all authenticated users).

POST /auth/login - Login into the App

POST /auth/register - Register a new user into the App
```

## Authentication
The API uses Spring Security for authentication control. The following roles are available:

```
USER -> Standard user role for logged-in users.
ADMIN -> Admin role for managing partners (registering new partners).
```
To access protected endpoints as an ADMIN user, provide the appropriate authentication credentials in the request header.

## Database
The project utilizes [PostgresSQL](https://www.postgresql.org/) as the database. The necessary database migrations are managed using Flyway.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request to the repository.

When contributing to this project, please follow the existing code style, [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/), and submit your changes in a separate branch.
