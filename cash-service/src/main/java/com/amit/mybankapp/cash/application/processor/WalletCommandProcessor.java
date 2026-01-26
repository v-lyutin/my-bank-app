package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.type.WalletOperationType;

import java.math.BigDecimal;

public interface WalletCommandProcessor {

    WalletOperationType walletOperationType();

    WalletOperationResponse process(BigDecimal amount);

}
