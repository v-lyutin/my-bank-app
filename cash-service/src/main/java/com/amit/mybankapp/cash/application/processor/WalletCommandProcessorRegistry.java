package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.commons.model.type.WalletOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WalletCommandProcessorRegistry {

    private final Map<WalletOperationType, WalletCommandProcessor> processors;

    @Autowired
    public WalletCommandProcessorRegistry(List<WalletCommandProcessor> processors) {
        this.processors = processors.stream().collect(Collectors.toMap(WalletCommandProcessor::walletOperationType, Function.identity()));
    }

    public WalletCommandProcessor get(WalletOperationType walletOperationType) {
        Objects.requireNonNull(walletOperationType, "Unsupported walletOperationType: " + walletOperationType);
        return this.processors.get(walletOperationType);
    }

}
