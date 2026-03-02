package com.amit.mybank.notifications.application.impl;

import com.amit.mybank.notifications.application.NotificationSender;
import com.amit.mybank.notifications.messaging.event.TransferCreatedEvent;
import com.amit.mybank.notifications.messaging.event.WalletOperationCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingNotificationSender implements NotificationSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingNotificationSender.class);

    @Override
    public void sendWalletOperationCompleted(WalletOperationCompletedEvent walletOperationCompletedEvent) {
        LOGGER.info(
                "NOTIFICATION: wallet operation completed. operationId={}, type={}, customerId={}, walletId={}, amount={}",
                walletOperationCompletedEvent.operationId(),
                walletOperationCompletedEvent.operationName(),
                walletOperationCompletedEvent.customerId(),
                walletOperationCompletedEvent.walletId(),
                walletOperationCompletedEvent.amount()
        );
    }

    @Override
    public void sendTransferCreated(TransferCreatedEvent transferCreatedEvent) {
        LOGGER.info(
                "NOTIFICATION: transfer created. transferId={}, senderCustomerId={}, recipientCustomerId={}, amount={}, status={}",
                transferCreatedEvent.transferId(),
                transferCreatedEvent.senderCustomerId(),
                transferCreatedEvent.recipientCustomerId(),
                transferCreatedEvent.amount(),
                transferCreatedEvent.status()
        );
    }

}
