package com.amit.mybankapp.cash.application.client;

import com.amit.mybankapp.cash.application.client.dto.WalletResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface AccountsClient {

    WalletResponse deposit(UUID walletId, BigDecimal amount);

    WalletResponse withdraw(UUID walletId, BigDecimal amount);

    UUID getPrimaryWalletIdForCurrentCustomer();

}
