package com.amit.mybankapp.accounts.domain.wallet.vo;

import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InsufficientFundsException;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InvalidMoneyException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        if (amount.signum() < 0) {
            throw new InvalidMoneyException("amount must be >= 0");
        }
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public boolean isNegative() {
        return this.amount.signum() < 0;
    }

    public Money plus(Money otherMoney) {
        return new Money(this.amount.add(otherMoney.amount));
    }

    public Money minus(Money otherMoney) {
        BigDecimal result = this.amount.subtract(otherMoney.amount);

        if (result.signum() < 0) {
            throw new InsufficientFundsException();
        }

        return new Money(result);
    }

}
