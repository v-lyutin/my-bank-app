package com.amit.mybankapp.frontcontroller.application.usecase.util;

import com.amit.mybankapp.frontcontroller.application.exception.ValidationException;

import java.math.BigDecimal;

public final class MoneyValidator {

    public static BigDecimal requirePositiveAmount(BigDecimal amount) {
        if (amount == null) {
            throw new ValidationException("Amount must be provided");
        }
        if (amount.signum() <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }
        return amount;
    }

    private MoneyValidator() {
        throw new UnsupportedOperationException();
    }

}
