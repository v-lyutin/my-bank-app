package com.amit.mybank.notifications.api;

import com.amit.mybank.notifications.application.NotificationSender;
import com.amit.mybankapp.commons.model.event.TransferCreatedEvent;
import com.amit.mybankapp.commons.model.event.WalletOperationCompletedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/events")
public class NotificationEventsController {

    private final NotificationSender notificationSender;

    @Autowired
    public NotificationEventsController(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @PostMapping(path = "/wallet-operations/completed")
    public ResponseEntity<Void> walletOperationCompleted(@RequestBody WalletOperationCompletedEvent walletOperationCompletedEvent) {
        this.notificationSender.sendWalletOperationCompleted(walletOperationCompletedEvent);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(path = "/transfers/created")
    public ResponseEntity<Void> transferCreated(@RequestBody TransferCreatedEvent transferCreatedEvent) {
        this.notificationSender.sendTransferCreated(transferCreatedEvent);
        return ResponseEntity.accepted().build();
    }

}
