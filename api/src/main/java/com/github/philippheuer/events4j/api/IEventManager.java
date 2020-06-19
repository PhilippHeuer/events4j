package com.github.philippheuer.events4j.api;


import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;

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
    boolean hasEventHandler(Class eventHandlerClass);

    /**
     * Retrieves a EventHandler of the provided type
     *
     * @param eventHandlerClass the event handler class
     * @param <E> the eventHandler type
     * @return a reference to the requested event handler
     */
    <E> E getEventHandler(Class<E> eventHandlerClass);

}
