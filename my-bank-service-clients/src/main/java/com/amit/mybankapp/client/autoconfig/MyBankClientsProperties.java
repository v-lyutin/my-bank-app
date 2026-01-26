package com.amit.mybankapp.client.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mybank.clients")
public record MyBankClientsProperties(
        Service accountsService,
        Service transferService,
        Service walletService) {

    public record Service(String baseUrl) {}

}
