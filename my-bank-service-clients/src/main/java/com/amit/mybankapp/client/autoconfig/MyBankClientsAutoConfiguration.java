package com.amit.mybankapp.client.autoconfig;

import com.amit.mybankapp.client.restclient.RestClientAccountsClient;
import com.amit.mybankapp.client.restclient.RestClientNotificationsClient;
import com.amit.mybankapp.client.restclient.RestClientTransferClient;
import com.amit.mybankapp.client.restclient.RestClientWalletClient;
import com.amit.mybankapp.commons.client.AccountsClient;
import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.client.TransferClient;
import com.amit.mybankapp.commons.client.WalletClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties(value = MyBankClientsProperties.class)
public class MyBankClientsAutoConfiguration {

    private static final String HTTP_SCHEMA = "http://";

    @Bean
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder(RestClientBuilderConfigurer configurer) {
        return configurer.configure(RestClient.builder());
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.accounts-service", name = "service-id")
    public AccountsClient customerClient(@Qualifier(value = "loadBalancedRestClientBuilder") RestClient.Builder myBankLoadBalancedRestClientBuilder,
                                         MyBankClientsProperties properties) {
        RestClient restClient = myBankLoadBalancedRestClientBuilder
                .baseUrl(HTTP_SCHEMA + properties.accountsService().serviceId())
                .build();
        return new RestClientAccountsClient(restClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.transfer-service", name = "service-id")
    public TransferClient transferClient(@Qualifier(value = "loadBalancedRestClientBuilder") RestClient.Builder myBankLoadBalancedRestClientBuilder,
                                         MyBankClientsProperties properties) {
        RestClient restClient = myBankLoadBalancedRestClientBuilder
                .baseUrl(HTTP_SCHEMA + properties.transferService().serviceId())
                .build();
        return new RestClientTransferClient(restClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.wallet-service", name = "service-id")
    public WalletClient walletClient(@Qualifier(value = "loadBalancedRestClientBuilder") RestClient.Builder myBankLoadBalancedRestClientBuilder,
                                     MyBankClientsProperties properties) {
        RestClient restClient = myBankLoadBalancedRestClientBuilder
                .baseUrl(HTTP_SCHEMA + properties.walletService().serviceId())
                .build();
        return new RestClientWalletClient(restClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mybank.clients.notifications-service", name = "service-id")
    public NotificationsClient notificationsClient(@Qualifier(value = "loadBalancedRestClientBuilder") RestClient.Builder myBankLoadBalancedRestClientBuilder,
                                                   MyBankClientsProperties properties) {
        RestClient restClient = myBankLoadBalancedRestClientBuilder
                .baseUrl(HTTP_SCHEMA + properties.notificationsService().serviceId())
                .build();
        return new RestClientNotificationsClient(restClient);
    }

}
