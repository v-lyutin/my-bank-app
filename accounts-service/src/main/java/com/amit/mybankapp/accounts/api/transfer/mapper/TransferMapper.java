package com.amit.mybankapp.accounts.api.transfer.mapper;

import com.amit.mybankapp.accounts.application.transfer.model.TransferResult;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public CreateTransferResponse toTransferResponse(TransferResult result) {
        return new CreateTransferResponse(
                null,
                result.senderCustomerId(),
                result.recipientCustomerId(),
                result.amount(),
                null
        );
    }

}
