package com.amit.mybankapp.accounts.infrastructure.persistence.customer;

public final class SqlCustomerQuery {

    static final String FIND_BY_CUSTOMER_ID = """
            SELECT customer_id, login, first_name, last_name, birth_date
            FROM accounts.customers
            WHERE customer_id = :customerId
            """;

    static final String UPDATE_PROFILE_BY_CUSTOMER_ID = """
            UPDATE accounts.customers
            SET first_name = :firstName,
                last_name  = :lastName,
                birth_date = :birthDate
            WHERE customer_id = :customerId
            """;

    static final String FIND_RECIPIENT_CANDIDATES_EXCLUDING_CUSTOMER_ID = """
            SELECT customer_id, login, first_name, last_name
            FROM accounts.customers
            WHERE customer_id <> :excludeCustomerId
            ORDER BY last_name, first_name
            """;

    private SqlCustomerQuery() {
        throw new UnsupportedOperationException();
    }

}
