package com.amit.mybankapp.frontcontroller.application.service;

import com.amit.mybankapp.commons.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;
import com.amit.mybankapp.frontcontroller.application.service.util.MoneyValidator;
import com.amit.mybankapp.frontcontroller.application.usecase.DepositCashUseCase;
import com.amit.mybankapp.frontcontroller.port.WalletPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositCashApplicationService implements DepositCashUseCase {

    private final WalletPort walletPort;

    @Autowired
    public DepositCashApplicationService(WalletPort walletPort) {
        this.walletPort = walletPort;
    }

    @Override
    public void deposit(WalletOperationCommand command) {
        BigDecimal amount = MoneyValidator.requirePositiveAmount(command.amount());
        this.walletPort.deposit(new WalletOperationRequest(amount));
    }

}
