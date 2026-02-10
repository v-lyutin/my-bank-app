package com.amit.mybankapp.accounts.domain.customer.vo;

import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidProfileException;

import java.time.LocalDate;
import java.util.Objects;

public record Profile(String firstName, String lastName, LocalDate birthDate) {

    private static final int MAX_NAME_LENGTH = 64;

    public Profile {
        Objects.requireNonNull(firstName, "firstName must not be null");
        Objects.requireNonNull(lastName, "lastName must not be null");
        Objects.requireNonNull(birthDate, "birthDate must not be null");

        firstName = firstName.trim();
        lastName = lastName.trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            throw new InvalidProfileException("firstName and lastName must not be blank");
        }

        if (firstName.length() > MAX_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH) {
            throw new InvalidProfileException("firstName/lastName is too long (max " + MAX_NAME_LENGTH + ")");
        }

        if (hasControlChars(firstName) || hasControlChars(lastName)) {
            throw new InvalidProfileException("firstName/lastName contains invalid characters");
        }

        if (birthDate.isAfter(LocalDate.now().minusYears(18))) {
            throw new InvalidProfileException("profile owner must be at least 18 years old");
        }
    }

    private static boolean hasControlChars(String name) {
        return name.chars().anyMatch(Character::isISOControl);
    }

}
