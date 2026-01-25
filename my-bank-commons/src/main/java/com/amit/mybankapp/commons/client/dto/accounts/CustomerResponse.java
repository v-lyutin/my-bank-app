package com.amit.mybankapp.commons.client.dto.accounts;

import java.time.LocalDate;
import java.util.UUID;

public record CustomerResponse(
        UUID customerId,
        String login,
        String firstName,
        String lastName,
        LocalDate birthDate) {
}
