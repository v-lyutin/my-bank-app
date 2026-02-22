package com.amit.mybankapp.cash.application.listener;

import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class WalletOperationCompletedEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletOperationCompletedEventListener.class);

    private final NotificationsClient notificationsClient;

    @Autowired
    public WalletOperationCompletedEventListener(NotificationsClient notificationsClient) {
        this.notificationsClient = notificationsClient;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(WalletOperationCompletedEvent walletOperationCompletedEvent) {
        try {
            this.notificationsClient.sendWalletOperationCompletedEvent(walletOperationCompletedEvent);
        } catch (RuntimeException exception) {
            LOGGER.warn("Failed to send wallet operation notification. operationId={}", walletOperationCompletedEvent.operationId(), exception);
        }
    }

}
