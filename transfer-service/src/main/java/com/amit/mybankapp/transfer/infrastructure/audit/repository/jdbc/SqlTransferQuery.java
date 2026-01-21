package com.amit.mybankapp.transfer.infrastructure.audit.repository.jdbc;

public final class SqlTransferQuery {

    static final String INSERT_TRANSFER_OPERATION = """
            INSERT INTO transfers.transfer_operations (
                transfer_id,
                sender_customer_id,
                recipient_customer_id,
                amount,
                status,
                created_at
            ) VALUES (
                :transferId,
                :senderCustomerId,
                :recipientCustomerId,
                :amount,
                :status,
                :createdAt
            )
            """;

    static final String FIND_BY_TRANSFER_ID = """
            SELECT transfer_id, sender_customer_id, recipient_customer_id, amount, status, created_at
            FROM transfers.transfer_operations
            WHERE transfer_id = :transferId
            """;

    private SqlTransferQuery() {
        throw new UnsupportedOperationException();
    }

}
