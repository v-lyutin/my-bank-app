package com.amit.mybankapp.transfer.application.client.notifications;

import com.amit.mybankapp.transfer.application.client.notifications.dto.NotificationRequest;

import java.math.BigDecimal;
import java.util.UUID;

public interface NotificationsClient {

    void notify(NotificationRequest request);

    default void sendTransferSent(UUID customerId, BigDecimal amount) {
        this.notify(NotificationRequest.transferSent(customerId, amount));
    }

    default void sendTransferReceived(UUID customerId, BigDecimal amount) {
        this.notify(NotificationRequest.transferReceived(customerId, amount));
    }

}
