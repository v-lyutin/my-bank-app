-- liquibase formatted sql

-- changeset v-lyutin:create-wallets-table
CREATE TABLE ${schemaName}.wallets (
  wallet_id   UUID PRIMARY KEY,
  customer_id UUID NOT NULL,
  balance     NUMERIC(19,2) NOT NULL DEFAULT 0 CHECK (balance >= 0),

  CONSTRAINT fk_wallets_customer FOREIGN KEY (customer_id) REFERENCES ${schemaName}.customers (customer_id) ON DELETE CASCADE
);

CREATE INDEX idx_wallets_customer_id ON ${schemaName}.wallets (customer_id);

-- rollback DROP TABLE ${schemaName}.wallets