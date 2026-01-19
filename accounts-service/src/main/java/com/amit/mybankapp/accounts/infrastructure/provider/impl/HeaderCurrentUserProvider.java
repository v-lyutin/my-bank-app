package com.amit.mybankapp.accounts.infrastructure.provider.impl;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.infrastructure.provider.CurrentUserProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Component
public class HeaderCurrentUserProvider implements CurrentUserProvider {

    private static final String HEADER = "X-User-Id";

    @Override
    public CustomerId currentUserId() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new IllegalStateException("No request context");
        }

        String raw = servletRequestAttributes.getRequest().getHeader(HEADER);
        if (!StringUtils.hasText(raw)) {
            throw new IllegalArgumentException("Missing header: " + HEADER);
        }

        try {
            return CustomerId.of(UUID.fromString(raw.trim()));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid UUID in header " + HEADER + ": " + raw);
        }
    }

}
