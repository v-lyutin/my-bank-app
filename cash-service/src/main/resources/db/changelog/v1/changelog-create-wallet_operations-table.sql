-- liquibase formatted sql

-- changeset v-lyutin:create-wallet_operations-table
CREATE TABLE IF NOT EXISTS ${schemaName}.wallet_operations (
    operation_id UUID PRIMARY KEY,
    wallet_id UUID NULL,
    customer_id UUID NOT NULL,
    operation_type, VARCHAR(16) NOT NULL,
    amount NUMERIC(19,2) NOT NULL CHECK (amount > 0),
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- rollback DROP TABLE ${schemaName}.cash_operations;