package com.amit.mybankapp.client.autoconfig;

import com.amit.mybankapp.client.restclient.RestClientAccountsClient;
import com.amit.mybankapp.commons.client.AccountsClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(value = MyBankClientsProperties.class)
public class MyBankClientsAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.accounts-service", name = "base-url")
    public AccountsClient customerClient(RestClient.Builder builder, MyBankClientsProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.accountsService().baseUrl())
                .build();
        return new RestClientAccountsClient(restClient);
    }

}
