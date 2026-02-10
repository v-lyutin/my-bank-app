package com.amit.mybankapp.accounts.api.wallet;

import com.amit.mybankapp.accounts.api.wallet.mapper.WalletMapper;
import com.amit.mybankapp.accounts.application.wallet.WalletUseCase;
import com.amit.mybankapp.accounts.application.wallet.model.WalletOperationResult;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationRequest;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class WalletResource {

    private final WalletUseCase walletUseCase;

    private final WalletMapper walletMapper;

    @Autowired
    public WalletResource(WalletUseCase walletUseCase, WalletMapper walletMapper) {
        this.walletUseCase = walletUseCase;
        this.walletMapper = walletMapper;
    }

    // front-ui only — internal
    @GetMapping(path = "/wallets/me")
    public ResponseEntity<WalletResponse> getPrimaryWalletForCurrentUser() {
        Wallet wallet = this.walletUseCase.getPrimaryWalletForCurrentUser();
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

    // cash-service only — internal
    @PostMapping(path = "/internal/wallets/{customerId}/deposits")
    public ResponseEntity<WalletOperationResponse> deposit(@PathVariable(name = "customerId") UUID customerId,
                                                           @Valid @RequestBody WalletOperationRequest request) {
        WalletOperationResult walletOperationResult = this.walletUseCase.deposit(CustomerId.of(customerId), Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletOperationResponse(walletOperationResult));
    }

    // cash-service only — internal
    @PostMapping(path = "/internal/wallets/{customerId}/withdrawals")
    public ResponseEntity<WalletOperationResponse> withdraw(@PathVariable(name = "customerId") UUID customerId,
                                                            @Valid @RequestBody WalletOperationRequest request) {
        WalletOperationResult walletOperationResult = this.walletUseCase.withdraw(CustomerId.of(customerId), Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletOperationResponse(walletOperationResult));
    }

}
