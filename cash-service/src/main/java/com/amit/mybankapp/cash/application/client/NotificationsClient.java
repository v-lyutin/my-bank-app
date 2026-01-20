package com.amit.mybankapp.cash.application.client;

import com.amit.mybankapp.cash.application.client.dto.NotificationRequest;

public interface NotificationsClient {

    void sendDepositCreatedNotification(NotificationRequest request);

    void sendWithdrawalCreatedNotification(NotificationRequest request);

}
