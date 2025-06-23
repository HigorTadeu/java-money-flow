# Informações do Projeto

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

## Variáris de Ambiente
Deve ser configurada as variáveis de ambiente para que a aplicação fucnione corretamente

### Entradas
DB_HOST, DB_PORT, DB_BASE, DB_USER, DB_PASS
