-- Inserção do usuário
INSERT INTO users (id, name, email, login, password, status, role) VALUES (gen_random_uuid(), 'Administrator', 'admin@email.com', 'admin', '$2a$10$WG9kEmAUsx9cTIBmdEynVOXthtZ/VmFTNubCutii5foQDoRAkq3eS', 'ACTIVE', 'ADMIN');

-- Inserção da carteira e captura do ID
INSERT INTO wallets (id, name, description, active) VALUES (gen_random_uuid(), 'Minha Carteira', 'Carteira física', true)


-- Janeiro 2024
INSERT INTO transactions (id, description, amount, transaction_date, type, category_income, category_expense, observation, wallet_id) VALUES (gen_random_uuid(), 'Salário mensal', 4500.00, '2024-01-05', 'INCOME', 'SALARY', null, 'Pagamento salário Janeiro', (SELECT id FROM wallets w WHERE w.name = 'Minha Carteira'));
