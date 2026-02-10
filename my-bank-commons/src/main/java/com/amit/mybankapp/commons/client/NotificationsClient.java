package com.amit.mybankapp.commons.client;

import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;

public interface NotificationsClient {

    void sendWalletOperationCompletedEvent(WalletOperationCompletedEvent walletOperationCompletedEvent);

    void sendTransferCreatedEvent(TransferCreatedEvent transferCreatedEvent);

}
