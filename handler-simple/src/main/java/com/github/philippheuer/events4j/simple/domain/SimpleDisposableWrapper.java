package com.github.philippheuer.events4j.simple.domain;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@ToString
public class SimpleDisposableWrapper implements IEventSubscription {

    private final IDisposable disposable;

    private final String id;

    @SuppressWarnings("rawtypes")
    private final Class eventType;

    @SuppressWarnings("rawtypes")
    private final Consumer consumer;

    @ToString.Exclude
    private final Map<String, IEventSubscription> activeSubscriptions;

    @SuppressWarnings("rawtypes")
    public SimpleDisposableWrapper(IDisposable disposable, String id, Class eventType, Consumer consumer, Map<String, IEventSubscription> activeSubscriptions) {
        this.disposable = disposable;
        this.id = id;
        this.eventType = eventType;
        this.consumer = consumer;
        this.activeSubscriptions = activeSubscriptions;

        activeSubscriptions.put(id, this);
    }

    /**
     * Dispose
     */
    @Override
    public void dispose() {
        if (!disposable.isDisposed()) {
            // remove consumer
            disposable.dispose();

            // remove from active subscriptions
            activeSubscriptions.remove(id);
        }
    }

    @Override
    public boolean isDisposed() {
        return disposable.isDisposed();
    }

}
