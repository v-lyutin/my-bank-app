package com.amit.mybankapp.accounts.api.wallet;

import com.amit.mybankapp.accounts.api.wallet.dto.request.MoneyOperationRequest;
import com.amit.mybankapp.accounts.api.wallet.dto.response.WalletResponse;
import com.amit.mybankapp.accounts.api.wallet.mapper.WalletMapper;
import com.amit.mybankapp.accounts.application.WalletUseCase;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.accounts.domain.wallet.vo.Money;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // cash-service only — internal
    @PostMapping(path = "/me/deposits")
    public ResponseEntity<WalletResponse> deposit(@Valid @RequestBody MoneyOperationRequest request) {
        Wallet wallet = this.walletUseCase.deposit(Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

    // cash-service only — internal
    @PostMapping(path = "/me/withdrawals")
    public ResponseEntity<WalletResponse> withdraw(@Valid @RequestBody MoneyOperationRequest request) {
        Wallet wallet = this.walletUseCase.withdraw(Money.of(request.amount()));
        return ResponseEntity.ok(this.walletMapper.toWalletResponse(wallet));
    }

}
