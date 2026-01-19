package com.amit.mybankapp.accounts.api.dto.response;

import java.util.UUID;

public record CustomerLookupResponse(
        UUID userId,
        String login,
        String firstName,
        String lastName) {
}
