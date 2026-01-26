package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.cash.application.exception.WalletOperationExecutionException;
import com.amit.mybankapp.cash.application.model.WalletOperationCommand;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessorRegistry;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletOperationUseCase {

    private final WalletCommandProcessorRegistry walletCommandProcessorRegistry;

    private final WalletOperationAudit walletOperationAudit;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public WalletOperationUseCase(WalletCommandProcessorRegistry walletCommandProcessorRegistry,
                                  WalletOperationAudit walletOperationAudit,
                                  ApplicationEventPublisher applicationEventPublisher) {
        this.walletCommandProcessorRegistry = walletCommandProcessorRegistry;
        this.walletOperationAudit = walletOperationAudit;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public WalletOperationResponse deposit(BigDecimal amount) {
        return this.executeWithAudit(new WalletOperationCommand(WalletOperationType.DEPOSIT, amount));
    }

    public WalletOperationResponse withdraw(BigDecimal amount) {
        return this.executeWithAudit(new WalletOperationCommand(WalletOperationType.WITHDRAW, amount));
    }

    public WalletOperationResponse executeWithAudit(WalletOperationCommand command) {
        UUID operationId = UUID.randomUUID();
        BigDecimal amount = command.amount();

        try {
            WalletCommandProcessor walletCommandProcessor = this.walletCommandProcessorRegistry.get(command.walletOperationType());
            WalletOperationResponse walletOperationResponse = walletCommandProcessor.process(amount);
            this.walletOperationAudit.accepted(
                    operationId,
                    command.walletOperationType().name(),
                    walletOperationResponse.walletId(),
                    walletOperationResponse.customerId(),
                    amount
            );

            WalletOperationResponse enrichedWalletOperationResponse = enrichWithOperationId(operationId, walletOperationResponse);

            this.applicationEventPublisher.publishEvent(WalletOperationCompletedEvent.from(enrichedWalletOperationResponse));

            return enrichedWalletOperationResponse;
        } catch (ApiException exception) {
            this.walletOperationAudit.rejected(operationId, command.walletOperationType().name(), null, null, amount);

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new WalletOperationExecutionException(operationId, exception);

        } catch (RuntimeException exception) {
            this.walletOperationAudit.rejected(operationId, command.walletOperationType().name(), null, null, amount);
            throw new WalletOperationExecutionException(operationId, exception);
        }
    }

    private static WalletOperationResponse enrichWithOperationId(UUID operationId, WalletOperationResponse walletOperationResponse) {
        return new WalletOperationResponse(
                operationId,
                walletOperationResponse.operationName(),
                walletOperationResponse.walletId(),
                walletOperationResponse.customerId(),
                walletOperationResponse.amount()
        );
    }

}
