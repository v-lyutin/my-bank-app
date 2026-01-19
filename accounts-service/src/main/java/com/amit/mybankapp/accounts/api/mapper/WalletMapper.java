package com.amit.mybankapp.accounts.api.mapper;

import com.amit.mybankapp.accounts.api.dto.response.WalletResponse;
import com.amit.mybankapp.accounts.domain.wallet.Wallet;
import org.springframework.stereotype.Component;

@Component
public final class WalletMapper {

    public WalletResponse toWalletResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getWalletId().value(),
                wallet.getCustomerId().value(),
                wallet.getBalance().amount()
        );
    }

}
