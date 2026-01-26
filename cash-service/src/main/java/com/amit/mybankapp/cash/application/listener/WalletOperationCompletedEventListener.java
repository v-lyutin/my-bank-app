package com.amit.mybankapp.cash.application.listener;

import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WalletOperationCompletedEventListener {

    private final NotificationsClient notificationsClient;

    @Autowired
    public WalletOperationCompletedEventListener(NotificationsClient notificationsClient) {
        this.notificationsClient = notificationsClient;
    }

    @EventListener
    public void on(WalletOperationCompletedEvent event) {
        // this.notificationsClient.sendWalletOperationCompleted(event);
    }

}
