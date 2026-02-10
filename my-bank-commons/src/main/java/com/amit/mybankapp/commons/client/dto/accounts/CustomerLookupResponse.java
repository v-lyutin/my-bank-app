package com.amit.mybankapp.commons.client.dto.accounts;

import java.util.UUID;

public record CustomerLookupResponse(
        UUID userId,
        String login,
        String firstName,
        String lastName) {
}
