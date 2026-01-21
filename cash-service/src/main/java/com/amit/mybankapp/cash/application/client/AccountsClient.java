package com.amit.mybankapp.cash.application.client;

import com.amit.mybankapp.cash.application.client.dto.WalletResponse;

import java.math.BigDecimal;

public interface AccountsClient {

    WalletResponse deposit(BigDecimal amount);

    WalletResponse withdraw(BigDecimal amount);

}
