package com.amit.mybankapp.cash.application.client.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record NotificationRequest(
        NotificationType type,
        UUID customerId,
        UUID walletId,
        BigDecimal amount) {

    public static NotificationRequest deposit(UUID customerId, UUID walletId, BigDecimal amount) {
        return new NotificationRequest(NotificationType.DEPOSIT, customerId, walletId, amount);
    }

    public static NotificationRequest withdraw(UUID customerId, UUID walletId, BigDecimal amount) {
        return new NotificationRequest(NotificationType.WITHDRAW, customerId, walletId, amount);
    }

    private enum NotificationType {

        DEPOSIT,

        WITHDRAW

    }

}
