package com.github.philippheuer.events4j.reactor.util;

import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import lombok.Getter;
import lombok.ToString;
import reactor.core.Disposable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ToString
public class ReactorDisposableWrapper implements Disposable, IEventSubscription {

    @Getter
    private final String id;

    @Getter
    @SuppressWarnings("rawtypes")
    private final Class eventType;

    @Getter
    @SuppressWarnings("rawtypes")
    private final Consumer consumer;

    @Getter
    private final Disposable disposable;

    @Getter
    @ToString.Exclude
    private final Map<String, IEventSubscription> activeSubscriptions;

    @SuppressWarnings("rawtypes")
    public ReactorDisposableWrapper(Disposable disposable, String id, Class eventType, Consumer consumer, Map<String, IEventSubscription> activeSubscriptions) {
        this.disposable = disposable;
        this.id = id;
        this.eventType = eventType;
        this.consumer = consumer;
        this.activeSubscriptions = activeSubscriptions;

        activeSubscriptions.put(id, this);
    }

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
