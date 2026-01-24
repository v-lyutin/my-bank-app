package com.amit.mybankapp.accounts.domain.customer.vo;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        Objects.requireNonNull(value, "customerId must not be null");
    }

    public static CustomerId of(UUID value) {
        return new CustomerId(value);
    }

}
