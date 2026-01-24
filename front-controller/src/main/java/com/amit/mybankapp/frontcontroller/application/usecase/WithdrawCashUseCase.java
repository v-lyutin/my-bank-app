package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.frontcontroller.application.command.WalletOperationCommand;

public interface WithdrawCashUseCase {

    void withdraw(WalletOperationCommand command);

}
