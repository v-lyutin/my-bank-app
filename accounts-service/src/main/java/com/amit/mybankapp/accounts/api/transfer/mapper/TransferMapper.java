package com.amit.mybankapp.accounts.api.transfer.mapper;

import com.amit.mybankapp.accounts.api.transfer.dto.response.TransferResponse;
import com.amit.mybankapp.accounts.application.transfer.model.TransferResult;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public TransferResponse toTransferResponse(TransferResult result) {
        return new TransferResponse(
                result.senderCustomerId(),
                result.recipientCustomerId(),
                result.amount()
        );
    }

}
