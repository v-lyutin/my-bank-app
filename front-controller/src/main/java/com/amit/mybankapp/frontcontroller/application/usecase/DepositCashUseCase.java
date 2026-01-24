package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;

public interface DepositCashUseCase {

    void deposit(WalletOperationCommand command);

}
