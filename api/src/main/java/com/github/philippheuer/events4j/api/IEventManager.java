package com.github.philippheuer.events4j.api;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface IEventManager extends AutoCloseable {

    /**
     * Publish a Event
     *
     * @param event Event
     */
    void publish(@NonNull Object event);

    /**
     * Register EventHandler
     *
     * @param eventHandler IEventHandler
     */
    void registerEventHandler(@NonNull IEventHandler eventHandler);

    /**
     * Registers a new consumer based default event handler if supported
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new Disposable of the given eventType
     */
    @NonNull
    <E> IDisposable onEvent(@NonNull Class<E> eventClass, @NonNull Consumer<E> consumer);

    /**
     * Get the ServiceMediator
     *
     * @return ServiceMediator
     */
    @NonNull
    IServiceMediator getServiceMediator();

    /**
     * Checks if a given eventHandler is registered / present
     *
     * @param eventHandlerClass the event handler class
     * @return boolean
     */
    boolean hasEventHandler(@NonNull Class<? extends IEventHandler> eventHandlerClass);

    /**
     * Retrieves a EventHandler of the provided type
     *
     * @param eventHandlerClass the event handler class
     * @param <E> the eventHandler type
     * @throws RuntimeException if no event handler of the provided type is registered
     * @return a reference to the requested event handler
     */
    @NonNull
    <E extends IEventHandler> E getEventHandler(@NonNull Class<E> eventHandlerClass);

    /**
     * Gets all registered event handlers
     *
     * @return a list of all registered event handlers
     */
    @NonNull
    List<IEventHandler> getEventHandlers();

    /**
     * Gets a list of all active subscriptions
     *
     * @return a list that holds IEventSubscription`s
     */
    @NonNull
    List<IEventSubscription> getActiveSubscriptions();

}
