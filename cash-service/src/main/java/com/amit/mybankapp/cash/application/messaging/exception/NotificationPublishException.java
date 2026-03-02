package com.amit.mybankapp.cash.application.messaging.exception;

public class NotificationPublishException extends RuntimeException {

    public NotificationPublishException(String message, Throwable cause) {
        super(message, cause);
    }

}