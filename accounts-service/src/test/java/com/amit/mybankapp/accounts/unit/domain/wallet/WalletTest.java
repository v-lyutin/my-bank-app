package com.amit.mybankapp.accounts.unit.domain.wallet;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InsufficientFundsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    @DisplayName(value = "Should create wallet when walletId customerId and balance are not null")
    void shouldCreateWalletWhenWalletIdCustomerIdAndBalanceAreNotNull() {
        WalletId walletId = new WalletId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Money balance = new Money(new BigDecimal("100.00"));

        Wallet wallet = new Wallet(walletId, customerId, balance);

        assertAll(
                () -> assertEquals(walletId, wallet.getWalletId()),
                () -> assertEquals(customerId, wallet.getCustomerId()),
                () -> assertEquals(balance, wallet.getBalance())
        );
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when walletId is null")
    void constructor_shouldThrowNullPointerExceptionWhenWalletIdIsNull() {
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Money balance = new Money(new BigDecimal("10.00"));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Wallet(null, customerId, balance)
        );

        assertEquals("walletId must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when customerId is null")
    void constructor_shouldThrowNullPointerExceptionWhenCustomerIdIsNull() {
        WalletId walletId = new WalletId(UUID.randomUUID());
        Money balance = new Money(new BigDecimal("10.00"));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Wallet(walletId, null, balance)
        );

        assertEquals("customerId must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when balance is null")
    void constructor_shouldThrowNullPointerExceptionWhenBalanceIsNull() {
        WalletId walletId = new WalletId(UUID.randomUUID());
        CustomerId customerId = new CustomerId(UUID.randomUUID());

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Wallet(walletId, customerId, null)
        );

        assertEquals("balance must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should deposit money and increase balance when amount is valid")
    void deposit_shouldIncreaseBalanceWhenAmountIsValid() {
        Wallet wallet = new Wallet(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                new Money(new BigDecimal("50.00"))
        );

        wallet.deposit(new Money(new BigDecimal("25.50")));

        assertEquals(new BigDecimal("75.50"), wallet.getBalance().amount());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when deposit amount is null")
    void deposit_shouldThrowNullPointerExceptionWhenAmountIsNull() {
        Wallet wallet = new Wallet(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                new Money(new BigDecimal("10.00"))
        );

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> wallet.deposit(null)
        );

        assertEquals("amount must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should withdraw money and decrease balance when amount is valid")
    void withdraw_shouldDecreaseBalanceWhenAmountIsValid() {
        Wallet wallet = new Wallet(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                new Money(new BigDecimal("100.00"))
        );

        wallet.withdraw(new Money(new BigDecimal("40.00")));

        assertEquals(new BigDecimal("60.00"), wallet.getBalance().amount());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when withdraw amount is null")
    void withdraw_shouldThrowNullPointerExceptionWhenAmountIsNull() {
        Wallet wallet = new Wallet(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                new Money(new BigDecimal("10.00"))
        );

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> wallet.withdraw(null)
        );

        assertEquals("amount must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InsufficientFundsException when withdrawing more than balance")
    void withdraw_shouldThrowInsufficientFundsExceptionWhenAmountIsGreaterThanBalance() {
        Wallet wallet = new Wallet(
                new WalletId(UUID.randomUUID()),
                new CustomerId(UUID.randomUUID()),
                new Money(new BigDecimal("10.00"))
        );

        assertThrows(
                InsufficientFundsException.class,
                () -> wallet.withdraw(new Money(new BigDecimal("20.00")))
        );
    }

}
