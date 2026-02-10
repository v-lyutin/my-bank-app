package com.amit.mybankapp.accounts.infrastructure.persistence.wallet;

public final class SqlWalletQuery {

    static final String FIND_PRIMARY_BY_CUSTOMER_ID = """
            SELECT wallet_id, customer_id, balance
            FROM accounts.wallets
            WHERE customer_id = :customerId
              AND is_primary = true
            """;

    static final String FIND_PRIMARY_BY_CUSTOMER_ID_FOR_UPDATE = """
            SELECT wallet_id, customer_id, balance
            FROM accounts.wallets
            WHERE customer_id = :customerId
              AND is_primary = true
            FOR UPDATE
            """;

    static final String UPDATE_BALANCE_BY_WALLET_ID = """
            UPDATE accounts.wallets
            SET balance = :balance
            WHERE wallet_id = :walletId
            """;

    private SqlWalletQuery() {
        throw new UnsupportedOperationException();
    }

}
