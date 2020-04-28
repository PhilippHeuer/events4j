package com.github.philippheuer.events4j.simple.domain;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import lombok.Getter;

import java.util.function.Consumer;

public class SimpleEventHandlerSubscription implements IDisposable {

    /**
     * is Disposed?
     */
    @Getter
    private boolean isDisposed = false;

    /**
     * Event Handler
     */
    private SimpleEventHandler simpleEventHandler;

    private Class eventClass;

    private Consumer eventConsumer;

    public SimpleEventHandlerSubscription(SimpleEventHandler simpleEventHandler, Class eventClass, Consumer consumer) {
        this.simpleEventHandler = simpleEventHandler;
        this.eventClass = eventClass;
        this.eventConsumer = consumer;
    }

    /**
     * Dispose
     */
    public void dispose() {
        if (!isDisposed) {
            // remove consumer
            if (eventConsumer != null) {
                simpleEventHandler.getConsumerBasedHandlers().get(eventClass.getCanonicalName()).remove(eventConsumer);
            }

            // disposed
            isDisposed = true;
        }
    }

}
