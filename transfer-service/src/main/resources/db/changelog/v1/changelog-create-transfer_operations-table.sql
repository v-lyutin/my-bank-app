-- liquibase formatted sql

-- changeset v-lyutin:transfer-002-create-transfer-operations
CREATE TABLE IF NOT EXISTS ${schemaName}.transfer_operations (
  transfer_id           UUID PRIMARY KEY,
  sender_customer_id    UUID NOT NULL,
  recipient_customer_id UUID NOT NULL,
  amount                NUMERIC(19,2) NOT NULL CHECK (amount > 0),
  status                VARCHAR(16) NOT NULL CHECK (status IN ('ACCEPTED', 'REJECTED')),
  created_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- rollback DROP TABLE IF EXISTS ${schemaName}.transfer_operations;
