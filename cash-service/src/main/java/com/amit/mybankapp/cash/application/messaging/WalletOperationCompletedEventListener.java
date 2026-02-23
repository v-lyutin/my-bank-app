package com.amit.mybankapp.cash.application.messaging;

import com.amit.mybankapp.cash.application.messaging.event.WalletOperationCompletedEvent;
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

    private final NotificationsProducer notificationsProducer;

    @Autowired
    public WalletOperationCompletedEventListener(NotificationsProducer notificationsProducer) {
        this.notificationsProducer = notificationsProducer;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onWalletOperationCompletedEvent(WalletOperationCompletedEvent event) {
        try {
            this.notificationsProducer.send(event);
        } catch (RuntimeException exception) {
            LOGGER.warn(
                    "Failed to publish wallet-operation-completed.v1 to Kafka. operationId={}",
                    event.operationId(),
                    exception
            );
        }
    }

}
