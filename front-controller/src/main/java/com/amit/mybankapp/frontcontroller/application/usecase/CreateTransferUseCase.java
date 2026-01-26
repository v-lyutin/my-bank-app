package com.amit.mybankapp.frontcontroller.application.usecase;

import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.TransferClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.frontcontroller.application.command.CreateTransferCommand;
import com.amit.mybankapp.frontcontroller.application.exception.ValidationException;
import com.amit.mybankapp.frontcontroller.application.result.CreateTransferResult;
import com.amit.mybankapp.frontcontroller.application.usecase.util.MoneyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateTransferUseCase {

    private final AccountsClient accountsClient;

    private final TransferClient transferClient;

    @Autowired
    public CreateTransferUseCase(AccountsClient accountsClient, TransferClient transferClient) {
        this.accountsClient = accountsClient;
        this.transferClient = transferClient;
    }

    public CreateTransferResult createTransfer(CreateTransferCommand command) {
        validate(command);

        UUID currentCustomerId = this.accountsClient.getCurrentCustomer().customerId();
        if (currentCustomerId.equals(command.recipientCustomerId())) {
            throw new ValidationException("You cannot transfer money to yourself");
        }

        CreateTransferRequest createTransferRequest = new CreateTransferRequest(command.recipientCustomerId(), command.amount());
        CreateTransferResponse createTransferResponse = this.transferClient.createTransfer(createTransferRequest);

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
