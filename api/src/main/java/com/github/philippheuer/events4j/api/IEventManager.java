package com.github.philippheuer.events4j.api;


import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;

import java.util.List;
import java.util.function.Consumer;

public interface IEventManager extends AutoCloseable {

    /**
     * Publish a Event
     *
     * @param event Event
     */
    void publish(Object event);

    /**
     * Register EventHandler
     *
     * @param eventHandler IEventHandler
     */
    void registerEventHandler(IEventHandler eventHandler);

    /**
     * Registers a new consumer based default event handler if supported
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new Disposable of the given eventType
     */
    <E extends Object> IDisposable onEvent(Class<E> eventClass, Consumer<E> consumer);

    /**
     * Get the ServiceMediator
     *
     * @return ServiceMediator
     */
    IServiceMediator getServiceMediator();

    /**
     * Checks if a fiven eventHandler is registered / present
     *
     * @param eventHandlerClass the event handler class
     * @return boolean
     */
    boolean hasEventHandler(Class<? extends IEventHandler> eventHandlerClass);

    /**
     * Retrieves a EventHandler of the provided type
     *
     * @param eventHandlerClass the event handler class
     * @param <E> the eventHandler type
     * @return a reference to the requested event handler
     */
    <E extends IEventHandler> E getEventHandler(Class<E> eventHandlerClass);

    /**
     * Gets all registered event handlers
     *
     * @return a list of all registered event handlers
     */
    List<IEventHandler> getEventHandlers();

    /**
     * Gets a list of all active subscriptions
     *
     * @return a list that holds IEventSubscription`s
     */
    List<IEventSubscription> getActiveSubscriptions();
}
