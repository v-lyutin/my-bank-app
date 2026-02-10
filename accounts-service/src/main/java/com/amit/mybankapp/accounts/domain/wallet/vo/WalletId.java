package com.amit.mybankapp.accounts.domain.wallet.vo;

import java.util.Objects;
import java.util.UUID;

public record WalletId(UUID value) {

    public WalletId {
        Objects.requireNonNull(value, "walletId must not be null");
    }

    public static WalletId of(UUID value) {
        return new WalletId(value);
    }

}
