package com.amit.mybank.notifications.messaging.dlq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class DeadLetterQueueHeaderProvider {

    private final int maxExceptionMessageBytes;

    public DeadLetterQueueHeaderProvider(int maxExceptionMessageBytes) {
        this.maxExceptionMessageBytes = maxExceptionMessageBytes;
    }

    public RecordHeaders build(ConsumerRecord<?, ?> record, Exception exception) {
        RecordHeaders recordHeaders = new RecordHeaders(record.headers().toArray());

        recordHeaders.add(DeadLetterQueueHeader.X_ORIGINAL_TOPIC, record.topic().getBytes(StandardCharsets.UTF_8));
        recordHeaders.add(DeadLetterQueueHeader.X_ORIGINAL_PARTITION, intToBytes(record.partition()));
        recordHeaders.add(DeadLetterQueueHeader.X_ORIGINAL_OFFSET, longToBytes(record.offset()));
        recordHeaders.add(DeadLetterQueueHeader.X_ORIGINAL_TIMESTAMP, longToBytes(record.timestamp()));
        recordHeaders.add(DeadLetterQueueHeader.X_EXCEPTION_CLASS, exception.getClass().getName().getBytes(StandardCharsets.UTF_8));

        String message = exception.getMessage();
        if (StringUtils.hasText(message)) {
            recordHeaders.add(DeadLetterQueueHeader.X_EXCEPTION_MESSAGE, truncateUtf8(message, maxExceptionMessageBytes));
        }

        return recordHeaders;
    }

    private static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static byte[] longToBytes(long value) {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    private static byte[] truncateUtf8(String value, int maxBytes) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= maxBytes) {
            return bytes;
        }

        byte[] truncatedBytes = new byte[maxBytes];
        System.arraycopy(bytes, 0, truncatedBytes, 0, maxBytes);
        return truncatedBytes;
    }

}
