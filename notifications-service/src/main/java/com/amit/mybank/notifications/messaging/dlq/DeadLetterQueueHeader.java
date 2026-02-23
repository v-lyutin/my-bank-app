package com.amit.mybank.notifications.messaging.dlq;

public final class DeadLetterQueueHeader {

    public static final String X_ORIGINAL_TOPIC = "x-original-topic";

    public static final String X_ORIGINAL_PARTITION = "x-original-partition";

    public static final String X_ORIGINAL_OFFSET = "x-original-offset";

    public static final String X_ORIGINAL_TIMESTAMP = "x-original-timestamp";

    public static final String X_EXCEPTION_CLASS = "x-exception-class";

    public static final String X_EXCEPTION_MESSAGE = "x-exception-message";

    private DeadLetterQueueHeader() {
        throw new UnsupportedOperationException();
    }

}
