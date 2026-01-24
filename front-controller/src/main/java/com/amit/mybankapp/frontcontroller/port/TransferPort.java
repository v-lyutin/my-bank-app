package com.amit.mybankapp.frontcontroller.port;

import com.amit.mybankapp.commons.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.dto.transfer.CreateTransferResponse;

public interface TransferPort {

    CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest);

}
