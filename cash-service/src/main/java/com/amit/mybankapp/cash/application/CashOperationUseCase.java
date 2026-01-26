package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.cash.application.exception.CashOperationExecutionException;
import com.amit.mybankapp.cash.application.model.CashCommand;
import com.amit.mybankapp.cash.application.model.type.WalletCommandType;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessorRegistry;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CashOperationUseCase {

    private final WalletCommandProcessorRegistry walletCommandProcessorRegistry;

    private final WalletOperationAudit walletOperationAudit;

    @Autowired
    public CashOperationUseCase(WalletCommandProcessorRegistry walletCommandProcessorRegistry,
                                WalletOperationAudit walletOperationAudit) {
        this.walletCommandProcessorRegistry = walletCommandProcessorRegistry;
        this.walletOperationAudit = walletOperationAudit;
    }

    public WalletOperationResponse deposit(BigDecimal amount) {
        return this.executeWithAudit(new CashCommand(WalletCommandType.DEPOSIT, amount));
    }

    public WalletOperationResponse withdraw(BigDecimal amount) {
        return this.executeWithAudit(new CashCommand(WalletCommandType.WITHDRAW, amount));
    }

    public WalletOperationResponse executeWithAudit(CashCommand command) {
        UUID operationId = UUID.randomUUID();
        BigDecimal amount = command.amount();

        try {
            WalletCommandProcessor walletCommandProcessor = this.walletCommandProcessorRegistry.get(command.type());
            WalletOperationResponse walletOperationResponse = walletCommandProcessor.process(amount);
            this.walletOperationAudit.accepted(
                    operationId,
                    command.type().name(),
                    walletOperationResponse.walletId(),
                    walletOperationResponse.customerId(),
                    amount
            );
            return enrichWithOperationId(operationId, walletOperationResponse);
        } catch (ApiException exception) {
            this.walletOperationAudit.rejected(operationId, command.type().name(), null, null, amount);

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new CashOperationExecutionException(operationId, exception);

        } catch (RuntimeException exception) {
            this.walletOperationAudit.rejected(operationId, command.type().name(), null, null, amount);
            throw new CashOperationExecutionException(operationId, exception);
        }
    }

    private static WalletOperationResponse enrichWithOperationId(UUID operationId, WalletOperationResponse walletOperationResponse) {
        return new WalletOperationResponse(
                operationId,
                walletOperationResponse.operationName(),
                walletOperationResponse.walletId(),
                walletOperationResponse.customerId(),
                walletOperationResponse.amount(),
                walletOperationResponse.status()
        );
    }

}
