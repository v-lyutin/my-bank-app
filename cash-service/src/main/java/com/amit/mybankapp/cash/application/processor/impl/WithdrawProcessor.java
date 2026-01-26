package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.model.type.WalletCommandType;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
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
    public WalletCommandType type() {
        return WalletCommandType.WITHDRAW;
    }

    @Override
    public WalletOperationResponse process(BigDecimal amount) {
        WalletOperationResponse walletOperationResponse = this.accountsClient.withdraw(amount);
        // this.notificationsClient.sendWithdrawalCreatedNotification(NotificationRequest.withdraw(walletResponse.customerId(), walletResponse.walletId(), amount));
        return walletOperationResponse;
    }

}
