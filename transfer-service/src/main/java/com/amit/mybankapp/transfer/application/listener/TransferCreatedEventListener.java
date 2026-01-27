package com.amit.mybankapp.transfer.application.listener;

import com.amit.mybankapp.commons.client.NotificationsClient;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TransferCreatedEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferCreatedEventListener.class);

    private final NotificationsClient notificationsClient;

    @Autowired
    public TransferCreatedEventListener(NotificationsClient notificationsClient) {
        this.notificationsClient = notificationsClient;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(TransferCreatedEvent transferCreatedEvent) {
        try {
            //
        } catch (RuntimeException exception) {
            LOGGER.warn("Failed to send transfer notifications for transferId={}", transferCreatedEvent.transferId(), exception);
        }
    }

}
