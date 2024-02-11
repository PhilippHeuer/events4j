package com.github.philippheuer.events4j.simple.domain;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class SimpleEventHandlerSubscription implements IDisposable {

    @Getter
    private boolean isDisposed;

    @Getter(AccessLevel.NONE)
    private final SimpleEventHandler simpleEventHandler;

    private final Class<?> eventType;

    private final Consumer<?> consumer;

    public SimpleEventHandlerSubscription(SimpleEventHandler simpleEventHandler, Class<?> eventType, Consumer<?> consumer) {
        this.simpleEventHandler = simpleEventHandler;
        this.eventType = eventType;
        this.consumer = consumer;
    }

    /**
     * Dispose
     */
    public void dispose() {
        if (!isDisposed) {
            // remove consumer
            if (consumer != null) {
                simpleEventHandler.getConsumerBasedHandlers().get(eventType).remove(consumer);
            }

            // disposed
            isDisposed = true;
        }
    }

}
