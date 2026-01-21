package com.amit.mybankapp.transfer.infrastructure.client;

import com.amit.mybankapp.transfer.application.client.notifications.NotificationsClient;
import com.amit.mybankapp.transfer.application.client.notifications.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StubNotificationsClient implements NotificationsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubNotificationsClient.class);

    @Override
    public void notify(NotificationRequest request) {
        LOGGER.info(
                "Notification [{}] to customer {} for amount {}",
                request.type(),
                request.customerId(),
                request.amount()
        );
    }

}
