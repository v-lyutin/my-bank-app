package com.amit.mybankapp.frontcontroller.web.mapper;

import com.amit.mybankapp.frontcontroller.application.command.CreateTransferCommand;
import com.amit.mybankapp.frontcontroller.web.model.TransferForm;
import org.springframework.stereotype.Component;

@Component
public final class TransferWebMapper {

    public CreateTransferCommand toCreateTransferCommand(TransferForm transferForm) {
        return new CreateTransferCommand(transferForm.recipientCustomerId(), transferForm.amount());
    }

}
