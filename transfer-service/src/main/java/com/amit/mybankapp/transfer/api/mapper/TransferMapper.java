package com.amit.mybankapp.transfer.api.mapper;

import com.amit.mybankapp.transfer.api.dto.request.CreateTransferRequest;
import com.amit.mybankapp.transfer.api.dto.response.CreateTransferResponse;
import com.amit.mybankapp.transfer.application.model.TransferCommand;
import com.amit.mybankapp.transfer.application.model.TransferResult;
import org.springframework.stereotype.Component;

@Component
public final class TransferMapper {

    public TransferCommand toTransferCommand(CreateTransferRequest createTransferRequest) {
        return new TransferCommand(
                createTransferRequest.recipientCustomerId(),
                createTransferRequest.amount()
        );
    }

    public CreateTransferResponse toCreateTransferResponse(TransferResult transferResult) {
        return new CreateTransferResponse(
                transferResult.transferId(),
                transferResult.status()
        );
    }

}
