package com.amit.mybankapp.cash.application;

import com.amit.mybankapp.cash.application.exception.CashOperationExecutionException;
import com.amit.mybankapp.cash.application.model.CashCommand;
import com.amit.mybankapp.cash.application.model.CashResult;
import com.amit.mybankapp.cash.application.model.type.CashCommandType;
import com.amit.mybankapp.cash.application.model.type.CashStatus;
import com.amit.mybankapp.cash.application.processor.CashCommandProcessorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CashOperationUseCase {

    private final CashCommandProcessorRegistry cashCommandProcessorRegistry;

    private final CashOperationAudit cashOperationAudit;

    @Autowired
    public CashOperationUseCase(CashCommandProcessorRegistry cashCommandProcessorRegistry,
                                CashOperationAudit cashOperationAudit) {
        this.cashCommandProcessorRegistry = cashCommandProcessorRegistry;
        this.cashOperationAudit = cashOperationAudit;
    }

    public CashResult deposit(BigDecimal amount) {
        return this.execute(new CashCommand(CashCommandType.DEPOSIT, amount));
    }

    public CashResult withdraw(BigDecimal amount) {
        return this.execute(new CashCommand(CashCommandType.WITHDRAW, amount));
    }

    @Transactional
    public CashResult execute(CashCommand command) {
        UUID operationId = UUID.randomUUID();
        BigDecimal amount = command.amount();

        try {
            this.cashCommandProcessorRegistry.get(command.type()).process(amount);
            this.cashOperationAudit.accepted(operationId, command.type(), amount);
            return new CashResult(operationId, CashStatus.ACCEPTED);
        } catch (HttpClientErrorException exception) {
            this.cashOperationAudit.rejected(operationId, command.type(), amount);
            throw exception;
        } catch (HttpServerErrorException | ResourceAccessException exception) {
            this.cashOperationAudit.rejected(operationId, command.type(), amount);
            throw new CashOperationExecutionException(operationId, exception);
        } catch (RestClientResponseException exception) {
            this.cashOperationAudit.rejected(operationId, command.type(), amount);
            if (exception.getStatusCode().is4xxClientError()) {
                throw exception;
            }
            throw new CashOperationExecutionException(operationId, exception);
        } catch (RuntimeException exception) {
            this.cashOperationAudit.rejected(operationId, command.type(), amount);
            throw new CashOperationExecutionException(operationId, exception);
        }
    }

}
