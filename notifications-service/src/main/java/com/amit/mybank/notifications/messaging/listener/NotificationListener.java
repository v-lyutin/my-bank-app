package com.amit.mybank.notifications.messaging.listener;

import com.amit.mybank.notifications.application.NotificationSender;
import com.amit.mybank.notifications.messaging.event.TransferCreatedEvent;
import com.amit.mybank.notifications.messaging.event.WalletOperationCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final NotificationSender notificationSender;

    @Autowired
    public NotificationListener(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @KafkaListener(
            topics = "${mybank.kafka.topics.walletOperationCompleted}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onWalletOperationCompletedEvent(WalletOperationCompletedEvent event) {
        this.notificationSender.sendWalletOperationCompleted(event);
    }

    @KafkaListener(
            topics = "${mybank.kafka.topics.transferCreated}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTransferCreatedEvent(TransferCreatedEvent event) {
        this.notificationSender.sendTransferCreated(event);
    }

}
