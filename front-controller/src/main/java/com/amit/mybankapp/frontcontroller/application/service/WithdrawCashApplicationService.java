package com.amit.mybankapp.frontcontroller.application.service;

import com.amit.mybankapp.commons.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;
import com.amit.mybankapp.frontcontroller.application.service.util.MoneyValidator;
import com.amit.mybankapp.frontcontroller.application.usecase.WithdrawCashUseCase;
import com.amit.mybankapp.frontcontroller.port.WalletPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawCashApplicationService implements WithdrawCashUseCase {

    private final WalletPort walletPort;

    public WithdrawCashApplicationService(WalletPort walletPort) {
        this.walletPort = walletPort;
    }

    @Override
    public void withdraw(WalletOperationCommand command) {
        BigDecimal amount = MoneyValidator.requirePositiveAmount(command.amount());
        this.walletPort.withdraw(new WalletOperationRequest(amount));
    }

}
