package com.amit.mybankapp.transfer.infrastructure.client;

import com.amit.mybankapp.transfer.application.client.notifications.NotificationsClient;
import com.amit.mybankapp.transfer.application.client.notifications.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientNotificationsClient implements NotificationsClient {

    private final RestClient restClient;

    public RestClientNotificationsClient(@Qualifier(value = "notificationsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void notify(NotificationRequest request) {
        this.restClient
                .post()
                .uri("/notifications")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

}
