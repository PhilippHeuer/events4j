package com.github.philippheuer.events4j.api.service;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import lombok.NonNull;

import java.util.function.Consumer;

public interface IEventHandler extends AutoCloseable {

    /**
     * Publishes an Event
     *
     * @param event Event
     */
    void publish(@NonNull Object event);

    /**
     * Registers a new consumer based default event handler if supported
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new Disposable of the given eventType
     */
    <E> IDisposable onEvent(@NonNull Class<E> eventClass, @NonNull Consumer<E> consumer);

}
