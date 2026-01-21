package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.cash.application.model.type.CashCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CashCommandProcessorRegistry {

    private final Map<CashCommandType, CashCommandProcessor> processors;

    @Autowired
    public CashCommandProcessorRegistry(List<CashCommandProcessor> processors) {
        this.processors = processors.stream().collect(Collectors.toMap(CashCommandProcessor::type, Function.identity()));
    }

    public CashCommandProcessor get(CashCommandType type) {
        CashCommandProcessor cashCommandProcessor = this.processors.get(type);
        if (cashCommandProcessor == null) {
            // TODO: need to create custom exception or add handle this in GlobalExceptionHandler
            throw new IllegalArgumentException("Unsupported command type: " + type);
        }
        return cashCommandProcessor;
    }

}
