package com.amit.mybankapp.client.autoconfig;

import com.amit.mybankapp.client.restclient.RestClientAccountsClient;
import com.amit.mybankapp.client.restclient.RestClientNotificationsClient;
import com.amit.mybankapp.client.restclient.RestClientTransferClient;
import com.amit.mybankapp.client.restclient.RestClientWalletClient;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.client.TransferClient;
import com.amit.mybankapp.commons.client.WalletClient;
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

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.transfer-service", name = "base-url")
    public TransferClient transferClient(RestClient.Builder builder, MyBankClientsProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.transferService().baseUrl())
                .build();
        return new RestClientTransferClient(restClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.wallet-service", name = "base-url")
    public WalletClient walletClient(RestClient.Builder builder, MyBankClientsProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.walletService().baseUrl())
                .build();
        return new RestClientWalletClient(restClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.notifications-service", name = "base-url")
    public NotificationsClient notificationsClient(RestClient.Builder builder, MyBankClientsProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.notificationsService().baseUrl())
                .build();
        return new RestClientNotificationsClient(restClient);
    }

}
