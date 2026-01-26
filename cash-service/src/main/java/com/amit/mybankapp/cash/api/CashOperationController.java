package com.amit.mybankapp.cash.api;

import com.amit.mybankapp.cash.application.CashOperationUseCase;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/customers/me")
public class CashOperationController {

    private final CashOperationUseCase cashOperationUseCase;

    @Autowired
    public CashOperationController(CashOperationUseCase cashOperationUseCase) {
        this.cashOperationUseCase = cashOperationUseCase;
    }

    @PostMapping(path = "/deposits")
    public ResponseEntity<WalletOperationResponse> deposit(@Valid @RequestBody WalletOperationRequest request) {
        WalletOperationResponse walletOperationResponse = this.cashOperationUseCase.deposit(request.amount());
        return ResponseEntity.ok(walletOperationResponse);
    }

    @PostMapping(path = "/withdrawals")
    public ResponseEntity<WalletOperationResponse> withdraw(@Valid @RequestBody WalletOperationRequest request) {
        WalletOperationResponse walletOperationResponse = this.cashOperationUseCase.withdraw(request.amount());
        return ResponseEntity.ok(walletOperationResponse);
    }

}
