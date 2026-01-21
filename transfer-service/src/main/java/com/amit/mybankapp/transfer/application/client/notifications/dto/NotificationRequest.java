package com.amit.mybankapp.transfer.application.client.notifications.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record NotificationRequest(
        NotificationType type,
        UUID customerId,
        BigDecimal amount) {

    public static NotificationRequest transferSent(UUID customerId, BigDecimal amount) {
        return new NotificationRequest(NotificationType.TRANSFER_SENT, customerId, amount);
    }

    public static NotificationRequest transferReceived(UUID customerId, BigDecimal amount) {
        return new NotificationRequest(NotificationType.TRANSFER_RECEIVED, customerId, amount);
    }

    private enum NotificationType {

        TRANSFER_SENT,

        TRANSFER_RECEIVED

    }

}
