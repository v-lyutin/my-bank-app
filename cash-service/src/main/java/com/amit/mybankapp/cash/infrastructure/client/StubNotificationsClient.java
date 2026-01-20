package com.amit.mybankapp.cash.infrastructure.client;

import com.amit.mybankapp.cash.application.client.NotificationsClient;
import com.amit.mybankapp.cash.application.client.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StubNotificationsClient implements NotificationsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubNotificationsClient.class);

    @Override
    public void sendDepositCreatedNotification(NotificationRequest request) {
        LOGGER.info("[STUB] Notifications deposit: {}", request);
    }

    @Override
    public void sendWithdrawalCreatedNotification(NotificationRequest request) {
        LOGGER.info("[STUB] Notifications withdraw: {}", request);
    }

}
