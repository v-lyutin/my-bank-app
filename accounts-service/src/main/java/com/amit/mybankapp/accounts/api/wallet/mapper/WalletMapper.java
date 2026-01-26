package com.amit.mybankapp.accounts.api.wallet.mapper;

import com.amit.mybankapp.accounts.application.wallet.model.WalletOperationResult;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.client.dto.wallet.WalletResponse;
import org.springframework.stereotype.Component;

@Component
public final class WalletMapper {

    public WalletResponse toWalletResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getWalletId().value(),
                wallet.getBalance().amount()
        );
    }

    public WalletOperationResponse toWalletOperationResponse(WalletOperationResult walletOperationResult) {
        return new WalletOperationResponse(
                walletOperationResult.operationId(),
                walletOperationResult.operationName(),
                walletOperationResult.walletId(),
                walletOperationResult.customerId(),
                walletOperationResult.amount()
        );
    }

}
