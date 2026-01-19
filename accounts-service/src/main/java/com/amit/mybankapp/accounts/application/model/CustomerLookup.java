package com.amit.mybankapp.accounts.application.model;

import java.util.UUID;

public record CustomerLookup(
        UUID userId,
        String login,
        String firstName,
        String lastName) {
}
