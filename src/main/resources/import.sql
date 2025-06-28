INSERT INTO users (id, name, email, login, password, status, role) VALUES (gen_random_uuid(), 'Administrator', 'admin@email.com', 'admin', '$2a$10$0ZsUqNeGESujxCsNZaG60.0uQTpyDHqyGozEUOwc4zjvadS5lqJyu','ACTIVE','ADMIN')

INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Salário mensal', 4500.00, '2024-01-05', 'INCOME', 'OTHER', 'Pagamento salário Janeiro');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Supermercado Extra', 156.78, '2024-01-07', 'EXPENSE', 'FOOD', 'Compras da semana');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Conta de energia elétrica', 89.45, '2024-01-10', 'EXPENSE', 'HOME', 'Vencimento 10/01');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Freelance desenvolvimento web', 800.00, '2024-01-12', 'INCOME', 'OTHER', 'Projeto site institucional');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Combustível posto Shell', 120.50, '2024-01-15', 'EXPENSE', 'TRANSPORT', 'Abastecimento completo');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Dividendos ações Itaú', 45.30, '2024-01-18', 'INCOME', 'OTHER', 'Dividendos trimestrais');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Academia mensal', 85.00, '2024-01-20', 'EXPENSE', 'HEALTH', 'Mensalidade janeiro');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Venda produto usado', 250.00, '2024-01-22', 'INCOME', 'OTHER', 'Venda smartphone antigo');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Restaurante japonês', 95.80, '2024-01-25', 'EXPENSE', 'FOOD', 'Jantar com amigos');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Internet fibra ótica', 79.90, '2024-01-28', 'EXPENSE', 'HOME', 'Mensalidade internet');

INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Consultoria técnica', 1200.00, '2024-02-03', 'INCOME', 'OTHER', 'Consultoria sistema ERP');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Farmácia remédios', 67.45, '2024-02-05', 'EXPENSE', 'HEALTH', 'Medicamentos prescritos');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Salário mensal', 4500.00, '2024-02-05', 'INCOME', 'OTHER', 'Pagamento salário Fevereiro');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Mercado atacado', 234.67, '2024-02-08', 'EXPENSE', 'FOOD', 'Compras mensais');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Streaming Netflix', 32.90, '2024-02-10', 'EXPENSE', 'ENTERTAINMENT', 'Assinatura mensal');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Uber viagens', 45.20, '2024-02-12', 'EXPENSE', 'TRANSPORT', 'Corridas semana');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Rendimento poupança', 15.78, '2024-02-15', 'INCOME', 'OTHER', 'Rendimento mensal');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Material limpeza casa', 89.99, '2024-02-17', 'EXPENSE', 'HOME', 'Produtos limpeza');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Conta água e esgoto', 56.33, '2024-02-20', 'EXPENSE', 'HOME', 'Conta bimestral');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Cashback cartão', 23.45, '2024-02-22', 'INCOME', 'OTHER', 'Cashback compras');

INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Cinema ingresso', 28.00, '2024-02-25', 'EXPENSE', 'ENTERTAINMENT', 'Filme fim de semana');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Manutenção veículo', 450.00, '2024-02-28', 'EXPENSE', 'TRANSPORT', 'Revisão programada');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Projeto mobile app', 2500.00, '2024-03-02', 'INCOME', 'OTHER', 'Desenvolvimento aplicativo');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Salário mensal', 4500.00, '2024-03-05', 'INCOME', 'OTHER', 'Pagamento salário Março');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Padaria café manhã', 12.50, '2024-03-07', 'EXPENSE', 'FOOD', 'Café e pão');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Plano saúde', 320.00, '2024-03-08', 'EXPENSE', 'HEALTH', 'Mensalidade plano');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Livros técnicos', 89.90, '2024-03-10', 'EXPENSE', 'EDUCATION', 'Livros programação');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Móveis sala', 850.00, '2024-03-12', 'EXPENSE', 'HOME', 'Mesa e cadeiras');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Venda consultoria', 600.00, '2024-03-15', 'INCOME', 'OTHER', 'Consultoria banco dados');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Telefone celular', 49.90, '2024-03-18', 'EXPENSE', 'HOME', 'Plano móvel');

INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Aplicação CDB', 1000.00, '2024-03-20', 'EXPENSE', 'OTHER', 'Investimento CDB');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Lanchonete delivery', 35.50, '2024-03-22', 'EXPENSE', 'FOOD', 'Pedido iFood');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Curso online Udemy', 79.90, '2024-03-25', 'EXPENSE', 'EDUCATION', 'Curso React Native');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Reembolso seguro', 180.00, '2024-03-28', 'INCOME', 'OTHER', 'Reembolso sinistro');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Salário mensal', 4500.00, '2024-04-05', 'INCOME', 'OTHER', 'Pagamento salário Abril');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Supermercado Carrefour', 198.43, '2024-04-07', 'EXPENSE', 'FOOD', 'Compras quinzenais');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Seguro veículo', 89.50, '2024-04-10', 'EXPENSE', 'TRANSPORT', 'Parcela mensal seguro');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Dentista consulta', 120.00, '2024-04-12', 'EXPENSE', 'HEALTH', 'Limpeza dental');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Aluguel recebido', 1200.00, '2024-04-15', 'INCOME', 'OTHER', 'Aluguel apartamento');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Eletrodomésticos', 450.00, '2024-04-18', 'EXPENSE', 'HOME', 'Liquidificador e torradeira');

INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Transporte público', 65.00, '2024-04-20', 'EXPENSE', 'TRANSPORT', 'Recarga cartão ônibus');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Dividendos FII', 67.89, '2024-04-22', 'INCOME', 'OTHER', 'Dividendos imobiliário');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Estacionamento shopping', 8.00, '2024-04-25', 'EXPENSE', 'TRANSPORT', 'Estacionamento 4h');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Festa aniversário', 200.00, '2024-04-28', 'EXPENSE', 'ENTERTAINMENT', 'Festa 30 anos');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Salário mensal', 4500.00, '2024-05-05', 'INCOME', 'OTHER', 'Pagamento salário Maio');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Energia elétrica', 145.67, '2024-05-08', 'EXPENSE', 'HOME', 'Conta bimestral');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Taxi aeroporto', 65.00, '2024-05-10', 'EXPENSE', 'TRANSPORT', 'Ida aeroporto');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Bonificação trabalho', 1000.00, '2024-05-12', 'INCOME', 'OTHER', 'Bonificação trimestral');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Pizza delivery', 48.90, '2024-05-15', 'EXPENSE', 'FOOD', 'Pizza família');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Spotify premium', 21.90, '2024-05-18', 'EXPENSE', 'ENTERTAINMENT', 'Assinatura música');
INSERT INTO transactions (description, amount, transaction_date, type, category, observation) VALUES ('Universidade mensalidade', 850.00, '2024-05-20', 'EXPENSE', 'EDUCATION', 'Pós-graduação maio');