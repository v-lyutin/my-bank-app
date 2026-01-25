package com.amit.mybankapp.frontcontroller.port;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;

public interface TransferPort {

    CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest);

}
