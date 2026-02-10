package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.commons.client.WalletClient;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;
import com.amit.mybankapp.frontcontroller.application.usecase.util.MoneyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositCashUseCase {

    private final WalletClient walletClient;

    @Autowired
    public DepositCashUseCase(WalletClient walletClient) {
        this.walletClient = walletClient;
    }

    public void deposit(WalletOperationCommand command) {
        BigDecimal amount = MoneyValidator.requirePositiveAmount(command.amount());
        this.walletClient.deposit(new WalletOperationRequest(amount));
    }

}
