package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
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
    public WalletOperationType walletOperationType() {
        return WalletOperationType.DEPOSIT;
    }

    @Override
    public WalletOperationResponse process(BigDecimal amount) {
        WalletOperationResponse walletOperationResponse = this.accountsClient.deposit(new WalletOperationRequest(amount));
        // this.notificationsClient.sendDepositCreatedNotification(NotificationRequest.deposit(walletResponse.customerId(), walletResponse.walletId(), amount));
        return walletOperationResponse;
    }

}
