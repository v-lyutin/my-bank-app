package com.amit.mybank.notifications.application;

import com.amit.mybank.notifications.messaging.event.TransferCreatedEvent;
import com.amit.mybank.notifications.messaging.event.WalletOperationCompletedEvent;

public interface NotificationSender {

    void sendWalletOperationCompleted(WalletOperationCompletedEvent walletOperationCompletedEvent);

    void sendTransferCreated(TransferCreatedEvent transferCreatedEvent);

}
