package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.client.AccountsClient;
import com.amit.mybankapp.cash.application.client.NotificationsClient;
import com.amit.mybankapp.cash.application.client.dto.NotificationRequest;
import com.amit.mybankapp.cash.application.client.dto.WalletResponse;
import com.amit.mybankapp.cash.application.processor.CashCommandProcessor;
import com.amit.mybankapp.cash.application.model.type.CashCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DepositProcessor implements CashCommandProcessor {

    private final AccountsClient accountsClient;

    private final NotificationsClient notificationsClient;

    @Autowired
    public DepositProcessor(AccountsClient accountsClient, NotificationsClient notificationsClient) {
        this.accountsClient = accountsClient;
        this.notificationsClient = notificationsClient;
    }

    @Override
    public CashCommandType type() {
        return CashCommandType.DEPOSIT;
    }

    @Override
    public void process(UUID walletId, BigDecimal amount) {
        WalletResponse walletResponse = this.accountsClient.deposit(walletId, amount);
        this.notificationsClient.sendDepositCreatedNotification(NotificationRequest.deposit(walletResponse.customerId(), walletId, amount));
    }

}
