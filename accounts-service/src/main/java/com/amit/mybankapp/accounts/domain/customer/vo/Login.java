package com.amit.mybankapp.accounts.domain.customer.vo;

import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidLoginException;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public record Login(String value) {

    private static final int MAX_LENGTH = 128;

    private static final int MIN_LENGTH = 3;

    private static final Pattern ALLOWED_LOGIN_PATTERN = Pattern.compile("^[a-z0-9][a-z0-9._-]*[a-z0-9]$");

    private static final Set<String> RESERVED_LOGINS = Set.of("admin", "root", "system", "support", "api", "bank");

    public Login {
        Objects.requireNonNull(value, "value must not be null");

        String normalizedLogin = value.trim().toLowerCase(Locale.ROOT);

        if (normalizedLogin.length() < MIN_LENGTH || normalizedLogin.length() > MAX_LENGTH) {
            throw new InvalidLoginException("login length must be between " + MIN_LENGTH + " and " + MAX_LENGTH);
        }

        if (!ALLOWED_LOGIN_PATTERN.matcher(normalizedLogin).matches()) {
            throw new InvalidLoginException("login contains invalid characters");
        }

        if (RESERVED_LOGINS.contains(normalizedLogin)) {
            throw new InvalidLoginException("login is reserved");
        }

        value = normalizedLogin;
    }

    public static Login of(String value) {
        return new Login(value);
    }

}
