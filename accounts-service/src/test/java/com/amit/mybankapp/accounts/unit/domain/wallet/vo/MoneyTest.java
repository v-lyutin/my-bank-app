package com.amit.mybankapp.accounts.unit.domain.wallet.vo;

import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InsufficientFundsException;
import com.amit.mybankapp.accounts.domain.wallet.vo.exception.InvalidMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    @DisplayName(value = "Should create money when amount is zero")
    void shouldCreateMoneyWhenAmountIsZero() {
        Money money = new Money(BigDecimal.ZERO);

        assertEquals(new BigDecimal("0.00"), money.amount());
    }

    @Test
    @DisplayName(value = "Should create money when amount is positive")
    void shouldCreateMoneyWhenAmountIsPositive() {
        Money money = new Money(new BigDecimal("10.50"));

        assertEquals(new BigDecimal("10.50"), money.amount());
    }

    @Test
    @DisplayName(value = "Should round amount to scale 2 using HALF_UP rounding mode")
    void shouldRoundAmountToScale2UsingHalfUpRoundingMode() {
        Money money = new Money(new BigDecimal("10.555"));

        assertEquals(new BigDecimal("10.56"), money.amount());
    }

    @Test
    @DisplayName(value = "Should round amount down when third decimal digit is less than five")
    void shouldRoundAmountDownWhenThirdDecimalDigitIsLessThanFive() {
        Money money = new Money(new BigDecimal("10.554"));

        assertEquals(new BigDecimal("10.55"), money.amount());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when amount is null")
    void constructor_shouldThrowNullPointerExceptionWhenAmountIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Money(null)
        );

        assertEquals("amount must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidMoneyException when amount is negative")
    void constructor_shouldThrowInvalidMoneyExceptionWhenAmountIsNegative() {
        InvalidMoneyException exception = assertThrows(
                InvalidMoneyException.class,
                () -> new Money(new BigDecimal("-0.01"))
        );

        assertEquals("amount must be >= 0", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create money using factory method when amount is valid")
    void of_shouldCreateMoneyWhenAmountIsValid() {
        Money money = Money.of(new BigDecimal("25.00"));

        assertEquals(new BigDecimal("25.00"), money.amount());
    }

    @Test
    @DisplayName(value = "Should create zero money using zero factory method")
    void zero_shouldCreateZeroMoneyUsingZeroFactoryMethod() {
        Money money = Money.zero();

        assertEquals(new BigDecimal("0.00"), money.amount());
    }

    @Test
    @DisplayName(value = "Should return false for isNegative when amount is zero")
    void isNegative_shouldReturnFalseWhenAmountIsZero() {
        Money money = new Money(BigDecimal.ZERO);

        assertFalse(money.isNegative());
    }

    @Test
    @DisplayName(value = "Should return false for isNegative when amount is positive")
    void isNegative_shouldReturnFalseWhenAmountIsPositive() {
        Money money = new Money(new BigDecimal("1.00"));

        assertFalse(money.isNegative());
    }

    @Test
    @DisplayName(value = "Should add two money amounts correctly")
    void plus_shouldAddTwoMoneyAmountsCorrectly() {
        Money firstMoney = new Money(new BigDecimal("10.25"));
        Money secondMoney = new Money(new BigDecimal("5.30"));

        Money result = firstMoney.plus(secondMoney);

        assertEquals(new BigDecimal("15.55"), result.amount());
    }

    @Test
    @DisplayName(value = "Should return new money instance when plus is called")
    void plus_shouldReturnNewMoneyInstanceWhenCalled() {
        Money firstMoney = new Money(new BigDecimal("10.00"));
        Money secondMoney = new Money(new BigDecimal("5.00"));

        Money result = firstMoney.plus(secondMoney);

        assertNotSame(firstMoney, result);
        assertNotSame(secondMoney, result);
    }

    @Test
    @DisplayName(value = "Should subtract two money amounts correctly when result is non negative")
    void minus_shouldSubtractTwoMoneyAmountsCorrectlyWhenResultIsNonNegative() {
        Money firstMoney = new Money(new BigDecimal("10.00"));
        Money secondMoney = new Money(new BigDecimal("3.25"));

        Money result = firstMoney.minus(secondMoney);

        assertEquals(new BigDecimal("6.75"), result.amount());
    }

    @Test
    @DisplayName(value = "Should throw InsufficientFundsException when subtraction results in negative amount")
    void minus_shouldThrowInsufficientFundsExceptionWhenSubtractionResultsInNegativeAmount() {
        Money firstMoney = new Money(new BigDecimal("5.00"));
        Money secondMoney = new Money(new BigDecimal("10.00"));

        assertThrows(
                InsufficientFundsException.class,
                () -> firstMoney.minus(secondMoney)
        );
    }

    @Test
    @DisplayName(value = "Should preserve scale after plus operationName")
    void plus_shouldPreserveScaleAfterOperation() {
        Money firstMoney = new Money(new BigDecimal("1"));
        Money secondMoney = new Money(new BigDecimal("2"));

        Money result = firstMoney.plus(secondMoney);

        assertEquals(2, result.amount().scale());
        assertEquals(new BigDecimal("3.00"), result.amount());
    }

    @Test
    @DisplayName(value = "Should preserve scale after minus operationName")
    void minus_shouldPreserveScaleAfterOperation() {
        Money firstMoney = new Money(new BigDecimal("5"));
        Money secondMoney = new Money(new BigDecimal("2"));

        Money result = firstMoney.minus(secondMoney);

        assertEquals(2, result.amount().scale());
        assertEquals(new BigDecimal("3.00"), result.amount());
    }

}
