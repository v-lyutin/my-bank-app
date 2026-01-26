package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.model.type.WalletCommandType;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositProcessor implements WalletCommandProcessor {

    private final AccountsClient accountsClient;

    // private final NotificationsClient notificationsClient;

    @Autowired
    public DepositProcessor(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
        // this.notificationsClient = notificationsClient;
    }

    @Override
    public WalletCommandType type() {
        return WalletCommandType.DEPOSIT;
    }

    @Override
    public WalletOperationResponse process(BigDecimal amount) {
        WalletOperationResponse walletOperationResponse = this.accountsClient.deposit(amount);
        // this.notificationsClient.sendDepositCreatedNotification(NotificationRequest.deposit(walletResponse.customerId(), walletResponse.walletId(), amount));
        return walletOperationResponse;
    }

}
