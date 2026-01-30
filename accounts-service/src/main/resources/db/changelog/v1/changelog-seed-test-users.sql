-- liquibase formatted sql

-- changeset v-lyutin:seed-test-users context:test,dev
-- comment: Seed customers + primary wallets for local/dev tests

-- Требуется расширение uuid-ossp (обычно доступно в Postgres).
-- Если оно уже включено в проекте — можно удалить эти строки.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Данные одним блоком
WITH seed(customer_id, login, first_name, last_name, birth_date, balance) AS (
  VALUES
    ('110e74af-aaba-4b96-a56e-b268b56ece54'::uuid, 'vladislav.lyutin', 'Vladislav', 'Lyutin', '1995-01-10'::date, 10000.00::numeric),
    ('1069d840-1c6c-4b9e-8c7d-c5a88df238bf'::uuid, 'ivan.ivanov',     'Ivan',      'Ivanov', '1993-05-12'::date,  1000.00::numeric),
    ('991a4452-24b0-4829-9dff-e7aba99f6512'::uuid, 'walter.white',    'Walter',    'White',  '1990-09-20'::date,   500.00::numeric)
),

-- 1) customers upsert
upsert_customers AS (
  INSERT INTO ${schemaName}.customers (customer_id, login, first_name, last_name, birth_date)
  SELECT customer_id, login, first_name, last_name, birth_date
  FROM seed
  ON CONFLICT (customer_id) DO UPDATE
  SET login      = EXCLUDED.login,
      first_name = EXCLUDED.first_name,
      last_name  = EXCLUDED.last_name,
      birth_date = EXCLUDED.birth_date
  RETURNING customer_id
)

-- 2) wallets upsert (primary)
INSERT INTO ${schemaName}.wallets (wallet_id, customer_id, balance, is_primary)
SELECT
  uuid_generate_v5(uuid_ns_url(), 'primary-wallet:' || s.customer_id::text) AS wallet_id,
  s.customer_id,
  s.balance,
  true
FROM seed s
ON CONFLICT (wallet_id) DO UPDATE
SET balance    = EXCLUDED.balance,
    is_primary = EXCLUDED.is_primary;

-- rollback:
-- (пересчитываем те же wallet_id, чтобы удалить точно)
-- WITH seed(customer_id) AS (
--   VALUES
--     ('110e74af-aaba-4b96-a56e-b268b56ece54'::uuid),
--     ('1069d840-1c6c-4b9e-8c7d-c5a88df238bf'::uuid),
--     ('991a4452-24b0-4829-9dff-e7aba99f6512'::uuid)
-- )
-- DELETE FROM ${schemaName}.wallets
-- WHERE wallet_id IN (
--   SELECT uuid_generate_v5(uuid_ns_url(), 'primary-wallet:' || customer_id::text) FROM seed
-- );
-- DELETE FROM ${schemaName}.customers WHERE customer_id IN (
--   '110e74af-aaba-4b96-a56e-b268b56ece54',
--   '1069d840-1c6c-4b9e-8c7d-c5a88df238bf',
--   '991a4452-24b0-4829-9dff-e7aba99f6512'
-- );