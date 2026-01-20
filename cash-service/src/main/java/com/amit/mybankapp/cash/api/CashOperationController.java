package com.amit.mybankapp.cash.api;

import com.amit.mybankapp.cash.api.dto.CashOperationResponse;
import com.amit.mybankapp.cash.api.dto.MoneyOperationRequest;
import com.amit.mybankapp.cash.api.mapper.CashOperationMapper;
import com.amit.mybankapp.cash.application.CashOperationUseCase;
import com.amit.mybankapp.cash.application.model.CashResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/me")
public class CashOperationController {

    private final CashOperationUseCase cashOperationUseCase;

    private final CashOperationMapper cashOperationMapper;

    @Autowired
    public CashOperationController(CashOperationUseCase cashOperationUseCase, CashOperationMapper cashOperationMapper) {
        this.cashOperationUseCase = cashOperationUseCase;
        this.cashOperationMapper = cashOperationMapper;
    }

    @PostMapping(path = "/deposits")
    public ResponseEntity<CashOperationResponse> deposit(@Valid @RequestBody MoneyOperationRequest request) {
        CashResult cashResult = this.cashOperationUseCase.deposit(request.amount());
        return ResponseEntity.ok(this.cashOperationMapper.toCashOperationResponse(cashResult));
    }

    @PostMapping(path = "/withdrawals")
    public ResponseEntity<CashOperationResponse> withdraw(@Valid @RequestBody MoneyOperationRequest request) {
        CashResult cashResult = this.cashOperationUseCase.withdraw(request.amount());
        return ResponseEntity.ok(this.cashOperationMapper.toCashOperationResponse(cashResult));
    }

}
