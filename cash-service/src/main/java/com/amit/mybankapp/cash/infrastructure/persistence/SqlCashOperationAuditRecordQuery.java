package com.amit.mybankapp.cash.infrastructure.persistence;

public final class SqlCashOperationAuditRecordQuery {

    static final String INSERT = """
            INSERT INTO cash.cash_operations(operation_id, wallet_id, type, amount, status)
            VALUES (:operationId, :walletId, :type, :amount, :status)
            """;

    private SqlCashOperationAuditRecordQuery() {
        throw new UnsupportedOperationException();
    }

}
