package com.amit.mybank.notifications.application;

import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;

public interface NotificationSender {

    void sendWalletOperationCompleted(WalletOperationCompletedEvent walletOperationCompletedEvent);

    void sendTransferCreated(TransferCreatedEvent transferCreatedEvent);

}
