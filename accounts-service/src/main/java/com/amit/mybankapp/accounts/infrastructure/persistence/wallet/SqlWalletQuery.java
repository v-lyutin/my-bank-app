package com.amit.mybankapp.accounts.infrastructure.persistence.wallet;

public final class SqlWalletQuery {

    static final String FIND_BY_WALLET_ID_AND_CUSTOMER_ID = """
            SELECT wallet_id, customer_id, balance
            FROM accounts.wallets
            WHERE wallet_id = :walletId
              AND customer_id = :customerId
            """;

    static final String FIND_BY_WALLET_ID_AND_CUSTOMER_ID_FOR_UPDATE = """
            SELECT wallet_id, customer_id, balance
            FROM accounts.wallets
            WHERE wallet_id = :walletId
              AND customer_id = :customerId
            FOR UPDATE
            """;

    static final String FIND_BY_CUSTOMER_ID = """
            SELECT wallet_id, customer_id, balance
            FROM accounts.wallets
            WHERE customer_id = :customerId
            ORDER BY wallet_id
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
