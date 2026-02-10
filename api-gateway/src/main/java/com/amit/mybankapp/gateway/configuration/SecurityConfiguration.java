package com.amit.mybankapp.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final String ROLE_PREFIX = "ROLE_";

    private static final String REALM_ACCESS_CLAIM = "realm_access";

    private static final String ROLES_KEY = "roles";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers("/actuator/health/**").permitAll()

                        // cash-service endpoints (UI -> gateway -> cash)
                        .pathMatchers("/customers/me/deposits", "/customers/me/withdrawals").hasRole("CASH")

                        // transfer-service endpoint (UI -> gateway -> transfer)
                        .pathMatchers("/customers/me/transfers").hasRole("TRANSFER")

                        // accounts-service public endpoints (UI -> gateway -> accounts)
                        .pathMatchers(
                                "/customers/me",
                                "/customers/me/profile",
                                "/customers/me/recipient-candidates",
                                "/wallets/me"
                        ).hasRole("ACCOUNTS")

                        .anyExchange().denyAll()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractRealmRoles);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Object realmAccessClaim = jwt.getClaims().get(REALM_ACCESS_CLAIM);
        if (!(realmAccessClaim instanceof Map<?, ?> realmAccessMap)) {
            return authorities;
        }

        Object rolesValue = realmAccessMap.get(ROLES_KEY);
        if (!(rolesValue instanceof Collection<?> rolesCollection)) {
            return authorities;
        }

        for (Object roleObj : rolesCollection) {
            if (roleObj instanceof String role && !role.isBlank()) {
                authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
            }
        }

        return authorities;
    }

}
