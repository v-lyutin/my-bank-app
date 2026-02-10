package com.amit.mybankapp.accounts.unit.domain.wallet.vo;

import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletIdTest {

    @Test
    @DisplayName(value = "Should create walletId when value is not null")
    void shouldCreateWalletIdWhenValueIsNotNull() {
        UUID walletIdentifier = UUID.randomUUID();

        WalletId walletId = new WalletId(walletIdentifier);

        assertEquals(walletIdentifier, walletId.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when value is null")
    void constructor_shouldThrowNullPointerExceptionWhenValueIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WalletId(null)
        );

        assertEquals("walletId must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create walletId using factory method when value is not null")
    void of_shouldCreateWalletIdWhenValueIsNotNull() {
        UUID walletIdentifier = UUID.randomUUID();

        WalletId walletId = WalletId.of(walletIdentifier);

        assertEquals(walletIdentifier, walletId.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when factory method receives null value")
    void of_shouldThrowNullPointerExceptionWhenFactoryMethodReceivesNullValue() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> WalletId.of(null)
        );

        assertEquals("walletId must not be null", exception.getMessage());
    }

}
