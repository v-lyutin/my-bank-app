package com.amit.mybank.notifications.messaging.dlq;

import java.util.Map;

public final class DeadLetterQueueTopicResolver {

    private final Map<String, String> routing;

    private final String suffix;

    public DeadLetterQueueTopicResolver(Map<String, String> routing, String suffix) {
        this.routing = Map.copyOf(routing);
        this.suffix = suffix;
    }

    public String resolve(String sourceTopic) {
        return this.routing.getOrDefault(sourceTopic, sourceTopic + this.suffix);
    }

}
