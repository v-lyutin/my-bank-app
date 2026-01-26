package com.amit.mybankapp.cash.infrastructure.persistence;

public final class SqlWalletOperationAuditRecordQuery {

    static final String INSERT = """
        INSERT INTO cash.wallet_operations(
            operation_id, wallet_id, customer_id, operation_type, amount, status, created_at
        )
        VALUES (
            :operationId, :walletId, :customerId, :operationType, :amount, :status, COALESCE(:createdAt, now())
        )
        """;

    private SqlWalletOperationAuditRecordQuery() {
        throw new UnsupportedOperationException();
    }

}
