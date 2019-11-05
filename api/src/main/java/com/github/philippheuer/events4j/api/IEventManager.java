package com.github.philippheuer.events4j.api;


import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;

public interface IEventManager {

    /**
     * Publish a Event
     *
     * @param event Event
     */
    void publish(IEvent event);

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
     * Close
     */
    void close();
}
