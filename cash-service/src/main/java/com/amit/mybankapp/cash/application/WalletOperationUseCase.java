package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.apierrors.server.exception.ApiException;
import com.amit.mybankapp.cash.application.exception.WalletOperationExecutionException;
import com.amit.mybankapp.cash.application.messaging.event.WalletOperationCompletedEvent;
import com.amit.mybankapp.cash.application.model.WalletOperationCommand;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessor;
import com.amit.mybankapp.cash.application.processor.WalletCommandProcessorRegistry;
import com.amit.mybankapp.cash.infrastructure.provider.CurrentUserProvider;
import com.amit.mybankapp.commons.client.dto.wallet.WalletOperationResponse;
import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletOperationUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletOperationUseCase.class);

    private final WalletCommandProcessorRegistry walletCommandProcessorRegistry;

    private final WalletOperationAudit walletOperationAudit;

    private final CurrentUserProvider currentUserProvider;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public WalletOperationUseCase(WalletCommandProcessorRegistry walletCommandProcessorRegistry,
                                  WalletOperationAudit walletOperationAudit,
                                  CurrentUserProvider currentUserProvider,
                                  ApplicationEventPublisher applicationEventPublisher) {
        this.walletCommandProcessorRegistry = walletCommandProcessorRegistry;
        this.walletOperationAudit = walletOperationAudit;
        this.currentUserProvider = currentUserProvider;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public WalletOperationResponse deposit(BigDecimal amount) {
        return this.executeWithAudit(new WalletOperationCommand(WalletOperationType.DEPOSIT, amount));
    }

    @Transactional
    public WalletOperationResponse withdraw(BigDecimal amount) {
        return this.executeWithAudit(new WalletOperationCommand(WalletOperationType.WITHDRAW, amount));
    }

    public WalletOperationResponse executeWithAudit(WalletOperationCommand command) {
        UUID currentCustomerId = this.currentUserProvider.currentCustomerId();
        UUID operationId = UUID.randomUUID();
        BigDecimal amount = command.amount();

        LOGGER.info(
                "Wallet operation started: operationId={}, type={}, customerId={}, amount={}",
                operationId,
                command.walletOperationType(),
                currentCustomerId,
                amount
        );

        try {
            WalletCommandProcessor walletCommandProcessor = this.walletCommandProcessorRegistry.get(command.walletOperationType());
            WalletOperationResponse walletOperationResponse = walletCommandProcessor.process(currentCustomerId, amount);
            this.walletOperationAudit.accepted(
                    operationId,
                    command.walletOperationType().name(),
                    walletOperationResponse.walletId(),
                    walletOperationResponse.customerId(),
                    amount
            );

            LOGGER.info(
                    "Wallet operation succeeded: operationId={}, walletId={}, customerId={}, amount={}",
                    operationId,
                    walletOperationResponse.walletId(),
                    walletOperationResponse.customerId(),
                    walletOperationResponse.amount()
            );

            WalletOperationResponse enrichedWalletOperationResponse = enrichWithOperationId(operationId, walletOperationResponse);

            this.applicationEventPublisher.publishEvent(WalletOperationCompletedEvent.from(enrichedWalletOperationResponse));

            return enrichedWalletOperationResponse;
        } catch (ApiException exception) {
            LOGGER.error(
                    "Wallet operation failed with ApiException: operationId={}, type={}, status={}, message={}",
                    operationId,
                    command.walletOperationType(),
                    exception.status(),
                    exception.getMessage()
            );
            this.walletOperationAudit.rejected(operationId, command.walletOperationType().name(), null, currentCustomerId, amount);

            if (exception.status().is4xxClientError()) {
                throw exception;
            }

            throw new WalletOperationExecutionException(operationId, exception);

        } catch (RuntimeException exception) {
            LOGGER.error(
                    "Wallet operation failed with unexpected exception: operationId={}, type={}, customerId={}",
                    operationId,
                    command.walletOperationType(),
                    currentCustomerId,
                    exception
            );
            this.walletOperationAudit.rejected(operationId, command.walletOperationType().name(), null, currentCustomerId, amount);
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
