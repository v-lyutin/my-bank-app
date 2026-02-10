package com.amit.mybankapp.frontcontroller.infrastructure;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class FrontRestClientSecurityConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager oauth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .refreshToken()
                .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );
        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    public RestClientCustomizer userBearerTokenRestClientCustomizer(OAuth2AuthorizedClientManager manager) {
        return restClientBuilder ->
                restClientBuilder.requestInterceptor(userBearerTokenInterceptor(manager));
    }

    private ClientHttpRequestInterceptor userBearerTokenInterceptor(OAuth2AuthorizedClientManager manager) {
        return (request, body, execution) -> {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!(authentication instanceof OAuth2AuthenticationToken oauth2)) {
                return execution.execute(request, body);
            }

            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId(oauth2.getAuthorizedClientRegistrationId())
                    .principal(oauth2)
                    .build();

            OAuth2AuthorizedClient client = manager.authorize(authorizeRequest);

            if (client != null && client.getAccessToken() != null) {
                request.getHeaders().setBearerAuth(client.getAccessToken().getTokenValue());
            }
            return execution.execute(request, body);
        };
    }

}
