-- liquibase formatted sql

-- changeset v-lyutin:create-customers-table
CREATE TABLE ${schemaName}.customers (
  customer_id UUID PRIMARY KEY,
  login       VARCHAR(128) NOT NULL UNIQUE
                CHECK (length(login) BETWEEN 3 AND 128)
                CHECK (login = lower(login))
                CHECK (login ~ '^[a-z0-9][a-z0-9._-]*[a-z0-9]$'),
  first_name  VARCHAR(64) NOT NULL CHECK (length(trim(first_name)) > 0),
  last_name   VARCHAR(64) NOT NULL CHECK (length(trim(last_name)) > 0),
  birth_date  DATE NOT NULL
);
-- rollback DROP TABLE ${schemaName}.customers;