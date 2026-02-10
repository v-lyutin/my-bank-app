package com.amit.mybankapp.cash.application.processor.impl;

import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DepositProcessor implements WalletCommandProcessor {

    private final AccountsClient accountsClient;

    @Autowired
    public DepositProcessor(AccountsClient accountsClient) {
        this.accountsClient = accountsClient;
    }

    @Override
    public WalletOperationType walletOperationType() {
        return WalletOperationType.DEPOSIT;
    }

    @Override
    public WalletOperationResponse process(UUID customerId, BigDecimal amount) {
        return this.accountsClient.deposit(customerId, new WalletOperationRequest(amount));
    }

}
