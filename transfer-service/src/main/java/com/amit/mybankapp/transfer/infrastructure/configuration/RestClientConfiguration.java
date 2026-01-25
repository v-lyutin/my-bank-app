package com.amit.mybankapp.transfer.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    @Qualifier(value = "notificationsRestClient")
    public RestClient notificationsRestClient(RestClient.Builder builder,
                                              @Value(value = "${clients.notifications.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean
    @Qualifier(value = "accountsRestClient")
    public RestClient accountsRestClient(RestClient.Builder builder,
                                         @Value(value = "${clients.accounts.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

}
