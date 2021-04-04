package com.github.philippheuer.events4j.api.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@ToString
public class DisposableWrapper implements IEventSubscription {

    private final IDisposable disposable;

    private final String id;

    @SuppressWarnings("rawtypes")
    private final Class eventType;

    @SuppressWarnings("rawtypes")
    private final Consumer consumer;

    @ToString.Exclude
    private final Map<String, IEventSubscription> activeSubscriptions;

    @SuppressWarnings("rawtypes")
    public DisposableWrapper(IDisposable disposable, String id, Class eventType, Consumer consumer, Map<String, IEventSubscription> activeSubscriptions) {
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
            disposable.dispose();
            activeSubscriptions.remove(id);
        }
    }

    @Override
    public boolean isDisposed() {
        return disposable.isDisposed();
    }

}
