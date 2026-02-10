package com.amit.mybankapp.commons.client;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;

public interface TransferClient {

    CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest);

}
