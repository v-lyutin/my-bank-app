-- liquibase formatted sql

-- changeset v-lyutin:transfer-create-transfer-outbox
CREATE TABLE IF NOT EXISTS ${schemaName}.transfer_outbox (
    id                     UUID PRIMARY KEY,
    transfer_id            UUID NOT NULL,
    event_type             VARCHAR(64) NOT NULL,
    payload                JSONB NOT NULL,
    status                 VARCHAR(16) NOT NULL CHECK (status IN ('PENDING', 'PROCESSING', 'SENT', 'FAILED')),
    created_at             TIMESTAMPTZ NOT NULL DEFAULT now(),
    processing_started_at  TIMESTAMPTZ,
    processed_at           TIMESTAMPTZ
);

-- rollback DROP TABLE IF EXISTS ${schemaName}.transfer_outbox;