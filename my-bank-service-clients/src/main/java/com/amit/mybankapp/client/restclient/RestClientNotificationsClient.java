package com.amit.mybankapp.client.restclient;

import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;
import org.springframework.web.client.RestClient;

public class RestClientNotificationsClient implements NotificationsClient {

    private final RestClient restClient;

    public RestClientNotificationsClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void sendWalletOperationCompletedEvent(WalletOperationCompletedEvent walletOperationCompletedEvent) {
        this.restClient.post()
                .uri("/events/wallet-operations/completed")
                .body(walletOperationCompletedEvent)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void sendTransferCreatedEvent(TransferCreatedEvent transferCreatedEvent) {
        this.restClient.post()
                .uri("/events/transfers/created")
                .body(transferCreatedEvent)
                .retrieve()
                .toBodilessEntity();
    }

}
