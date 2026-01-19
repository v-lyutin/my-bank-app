package com.amit.mybankapp.accounts.api.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(
        UUID userId,
        String login,
        String firstName,
        String lastName,
        LocalDate birthDate) {
}
