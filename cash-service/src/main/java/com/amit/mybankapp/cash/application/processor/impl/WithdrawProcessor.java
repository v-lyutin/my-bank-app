package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WithdrawProcessor implements WalletCommandProcessor {

    private final AccountsClient accountsClient;

    // private final NotificationsClient notificationsClient;

    @Autowired
    public WithdrawProcessor(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
        // this.notificationsClient = notificationsClient;
    }

    @Override
    public WalletOperationType walletOperationType() {
        return WalletOperationType.WITHDRAW;
    }

    @Override
    public WalletOperationResponse process(BigDecimal amount) {
        WalletOperationResponse walletOperationResponse = this.accountsClient.withdraw(amount);
        // this.notificationsClient.sendWithdrawalCreatedNotification(NotificationRequest.withdraw(walletResponse.customerId(), walletResponse.walletId(), amount));
        return walletOperationResponse;
    }

}
