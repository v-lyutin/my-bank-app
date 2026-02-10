package com.amit.mybankapp.accounts.application.customer.model;

import java.util.UUID;

public record CustomerLookup(
        UUID userId,
        String login,
        String firstName,
        String lastName) {
}
