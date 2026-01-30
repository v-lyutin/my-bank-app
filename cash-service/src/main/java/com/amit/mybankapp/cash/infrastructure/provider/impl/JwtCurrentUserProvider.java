package com.amit.mybankapp.cash.infrastructure.provider.impl;

import com.amit.mybankapp.cash.infrastructure.provider.CurrentUserProvider;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class JwtCurrentUserProvider implements CurrentUserProvider {

    @Override
    public UUID currentCustomerId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT authentication");
        }

        String sub = jwtAuth.getToken().getSubject();
        if (!StringUtils.hasText(sub)) {
            throw new AuthenticationCredentialsNotFoundException("Missing sub claim");
        }

        return UUID.fromString(sub);
    }

}
