package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.frontcontroller.application.command.CreateTransferCommand;
import com.amit.mybankapp.frontcontroller.application.result.CreateTransferResult;

public interface CreateTransferUseCase {

    CreateTransferResult createTransfer(CreateTransferCommand command);

}
