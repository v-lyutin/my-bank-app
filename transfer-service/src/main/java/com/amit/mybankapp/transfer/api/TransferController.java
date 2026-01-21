package com.amit.mybankapp.transfer.api;

import com.amit.mybankapp.transfer.api.dto.request.CreateTransferRequest;
import com.amit.mybankapp.transfer.api.dto.response.CreateTransferResponse;
import com.amit.mybankapp.transfer.api.mapper.TransferMapper;
import com.amit.mybankapp.transfer.application.TransferUseCase;
import com.amit.mybankapp.transfer.application.model.TransferResult;
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

    @PostMapping
    public ResponseEntity<CreateTransferResponse> create(@Valid @RequestBody CreateTransferRequest request) {
        TransferResult result = this.transferUseCase.transfer(this.transferMapper.toTransferCommand(request));
        return ResponseEntity.ok(this.transferMapper.toCreateTransferResponse(result));
    }

}
