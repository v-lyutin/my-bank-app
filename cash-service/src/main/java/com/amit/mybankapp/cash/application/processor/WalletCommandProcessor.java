package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.cash.application.model.type.WalletCommandType;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;

import java.math.BigDecimal;

public interface WalletCommandProcessor {

    WalletCommandType type();

    WalletOperationResponse process(BigDecimal amount);

}
