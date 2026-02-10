package com.amit.mybankapp.transfer.api;

import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import com.amit.mybankapp.transfer.application.TransferUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/customers/me/transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;

    @Autowired
    public TransferController(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateTransferResponse> createTransfer(@Valid @RequestBody CreateTransferRequest request) {
        return ResponseEntity.ok().body(this.transferUseCase.createTransferWithAudit(request));
    }

}
