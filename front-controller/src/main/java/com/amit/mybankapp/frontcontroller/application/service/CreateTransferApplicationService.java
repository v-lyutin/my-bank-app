package com.amit.mybankapp.frontcontroller.application.service;

import com.amit.mybankapp.commons.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.frontcontroller.application.command.CreateTransferCommand;
import com.amit.mybankapp.frontcontroller.application.exception.ValidationException;
import com.amit.mybankapp.frontcontroller.application.result.CreateTransferResult;
import com.amit.mybankapp.frontcontroller.application.service.util.MoneyValidator;
import com.amit.mybankapp.frontcontroller.application.usecase.CreateTransferUseCase;
import com.amit.mybankapp.frontcontroller.port.AccountsPort;
import com.amit.mybankapp.frontcontroller.port.TransferPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateTransferApplicationService implements CreateTransferUseCase {

    private final AccountsPort accountsPort;

    private final TransferPort transferPort;

    @Autowired
    public CreateTransferApplicationService(AccountsPort accountsPort, TransferPort transferPort) {
        this.accountsPort = accountsPort;
        this.transferPort = transferPort;
    }

    @Override
    public CreateTransferResult createTransfer(CreateTransferCommand command) {
        validate(command);

        UUID currentCustomerId = this.accountsPort.getCurrentCustomer().customerId();
        if (currentCustomerId.equals(command.recipientCustomerId())) {
            throw new ValidationException("You cannot transfer money to yourself");
        }

        CreateTransferRequest createTransferRequest = new CreateTransferRequest(command.recipientCustomerId(), command.amount());
        CreateTransferResponse createTransferResponse = this.transferPort.createTransfer(createTransferRequest);

        return new CreateTransferResult(createTransferResponse.transferId(), createTransferResponse.status());
    }

    private static void validate(CreateTransferCommand command) {
        if (command == null) {
            throw new ValidationException("Transfer data must be provided");
        }
        if (command.recipientCustomerId() == null) {
            throw new ValidationException("Recipient must be specified");
        }
        MoneyValidator.requirePositiveAmount(command.amount());
    }

}
