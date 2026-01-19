package com.amit.mybankapp.accounts.api;

import com.amit.mybankapp.accounts.api.dto.request.MoneyOperationRequest;
import com.amit.mybankapp.accounts.api.dto.response.WalletResponse;
import com.amit.mybankapp.accounts.api.mapper.WalletMapper;
import com.amit.mybankapp.accounts.application.WalletUseCase;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/wallets")
public class WalletResource {

    private final WalletUseCase walletUseCase;

    private final WalletMapper walletMapper;

    @Autowired
    public WalletResource(WalletUseCase walletUseCase, WalletMapper walletMapper) {
        this.walletUseCase = walletUseCase;
        this.walletMapper = walletMapper;
    }

    @GetMapping
    public ResponseEntity<List<WalletResponse>> getWalletsForCurrentUser() {
        List<WalletResponse> response = this.walletUseCase.getWalletsForCurrentUser()
                .stream()
                .map(this.walletMapper::toWalletResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{walletId}")
    public ResponseEntity<WalletResponse> getWalletForCurrentUser(@PathVariable WalletId walletId) {
        Wallet wallet = this.walletUseCase.getWalletForCurrentUser(walletId);
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<WalletResponse> deposit(@PathVariable WalletId walletId,
                                                  @Valid @RequestBody MoneyOperationRequest request) {
        Wallet wallet = this.walletUseCase.deposit(walletId, Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

    @PostMapping(path = "/{walletId}/withdrawals")
    public ResponseEntity<WalletResponse> withdraw(@PathVariable WalletId walletId,
                                                   @Valid @RequestBody MoneyOperationRequest request) {
        Wallet wallet = this.walletUseCase.withdraw(walletId, Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

}
