package com.amit.mybankapp.cash.infrastructure.security;

import com.amit.mybankapp.client.autoconfig.MyBankClientsProperties;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.net.URI;

@Configuration
public class ServiceToServiceRestClientSecurityConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager clientCredentialsAuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                                  OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientService
                );

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public RestClientCustomizer serviceBearerTokenRestClientCustomizer(OAuth2AuthorizedClientManager authorizedClientManager, MyBankClientsProperties myBankClientsProperties) {
        String accountsBaseUrl = myBankClientsProperties.accountsService().baseUrl();
        URI accountsUri = URI.create(accountsBaseUrl);

        return restClientBuilder -> restClientBuilder.requestInterceptor((request, body, execution) -> {

            URI requestUri = request.getURI();
            boolean isAccountsCall = accountsUri.getHost() != null
                    && accountsUri.getHost().equalsIgnoreCase(requestUri.getHost())
                    && (accountsUri.getPort() == -1 || accountsUri.getPort() == requestUri.getPort());

            if (!isAccountsCall) {
                return execution.execute(request, body);
            }

            Authentication servicePrincipal = new AnonymousAuthenticationToken(
                    "key",
                    "cash-client",
                    AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
            );

            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("cash-service")
                    .principal(servicePrincipal)
                    .build();

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
                request.getHeaders().setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
            }

            return execution.execute(request, body);
        });
    }

}
