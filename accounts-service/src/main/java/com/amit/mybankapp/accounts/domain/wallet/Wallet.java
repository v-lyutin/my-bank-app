package com.amit.mybankapp.accounts.domain.wallet;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InvalidMoneyException;

import java.util.Objects;

public final class Wallet {

    private final WalletId walletId;

    private final CustomerId customerId;

    private Money balance;

    public Wallet(WalletId walletId, CustomerId customerId, Money balance) {
        this.walletId = Objects.requireNonNull(walletId, "walletId must not be null");
        this.customerId = Objects.requireNonNull(customerId, "customerId must not be null");
        this.balance = Objects.requireNonNull(balance, "balance must not be null");
    }

    public WalletId getWalletId() {
        return this.walletId;
    }

    public CustomerId getCustomerId() {
        return this.customerId;
    }

    public Money getBalance() {
        return this.balance;
    }

    public void deposit(Money amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.isNegative()) {
            throw new InvalidMoneyException("deposit amount must be > 0");
        }
        this.balance = this.balance.plus(amount);
    }

    public void withdraw(Money amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.isNegative()) {
            throw new InvalidMoneyException("withdraw amount must be > 0");
        }
        this.balance = this.balance.minus(amount);
    }

}
