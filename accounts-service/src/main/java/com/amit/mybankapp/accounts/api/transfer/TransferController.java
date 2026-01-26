package com.amit.mybankapp.accounts.api.transfer;

import com.amit.mybankapp.accounts.api.transfer.mapper.TransferMapper;
import com.amit.mybankapp.accounts.application.transfer.TransferUseCase;
import com.amit.mybankapp.accounts.application.transfer.model.TransferResult;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferUseCase transferUseCase;

    private final TransferMapper transferMapper;

    @Autowired
    public TransferController(TransferUseCase transferUseCase, TransferMapper transferMapper) {
        this.transferUseCase = transferUseCase;
        this.transferMapper = transferMapper;
    }

    // transfer-service only — internal
    @PostMapping
    public ResponseEntity<CreateTransferResponse> createTransfer(@Valid @RequestBody CreateTransferRequest request) {
        TransferResult result = this.transferUseCase.transfer(request.recipientCustomerId(), Money.of(request.amount()));
        return ResponseEntity.ok(this.transferMapper.toTransferResponse(result));
    }

}
