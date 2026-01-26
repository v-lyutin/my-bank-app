package com.amit.mybankapp.cash.application.processor;

import com.amit.mybankapp.cash.application.model.type.WalletCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WalletCommandProcessorRegistry {

    private final Map<WalletCommandType, WalletCommandProcessor> processors;

    @Autowired
    public WalletCommandProcessorRegistry(List<WalletCommandProcessor> processors) {
        this.processors = processors.stream().collect(Collectors.toMap(WalletCommandProcessor::type, Function.identity()));
    }

    public WalletCommandProcessor get(WalletCommandType walletCommandType) {
        Objects.requireNonNull(walletCommandType, "Unsupported command operationType: " + walletCommandType);
        return this.processors.get(walletCommandType);
    }

}
