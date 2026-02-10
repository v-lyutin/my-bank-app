package com.amit.mybankapp.accounts.infrastructure.sucurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String ROLE_PREFIX = "ROLE_";

    private static final String REALM_ACCESS_CLAIM = "realm_access";

    private static final String ROLES_KEY = "roles";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/health/**").permitAll()
                        .anyRequest().hasRole("ACCOUNTS")
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(this::extractRealmRolesAsAuthorities);
        return jwtAuthenticationConverter;
    }

    private Collection<GrantedAuthority> extractRealmRolesAsAuthorities(Jwt jwt) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Object realmAccessClaim = jwt.getClaims().get(REALM_ACCESS_CLAIM);
        if (!(realmAccessClaim instanceof Map<?, ?> realmAccessMap)) {
            return grantedAuthorities;
        }

        Object rolesValue = realmAccessMap.get(ROLES_KEY);
        if (!(rolesValue instanceof Collection<?> rolesCollection)) {
            return grantedAuthorities;
        }

        for (Object roleObject : rolesCollection) {
            if (roleObject instanceof String role && !role.isBlank()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
            }
        }

        return grantedAuthorities;
    }

}

