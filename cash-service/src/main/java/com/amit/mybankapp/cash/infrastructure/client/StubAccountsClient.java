package com.amit.mybankapp.cash.infrastructure.client;

import com.amit.mybankapp.cash.application.client.AccountsClient;
import com.amit.mybankapp.cash.application.client.dto.WalletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StubAccountsClient implements AccountsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubAccountsClient.class);

    @Override
    public WalletResponse deposit(UUID walletId, BigDecimal amount) {
        LOGGER.info("[STUB] Accounts deposit: walletId={}, amount={}", walletId, amount);
        return new WalletResponse(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN);
    }

    @Override
    public WalletResponse withdraw(UUID walletId, BigDecimal amount) {
        LOGGER.info("[STUB] Accounts withdraw: walletId={}, amount={}", walletId, amount);
        return new WalletResponse(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.TEN);
    }

    @Override
    public UUID getPrimaryWalletIdForCurrentCustomer() {
        LOGGER.info("[STUB] Getting primary walletId for current customer");
        return UUID.randomUUID();
    }

}
