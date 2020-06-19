package com.github.philippheuer.events4j.api.service;

public interface IEventHandler extends AutoCloseable {

    /**
     * Publish a Event
     *
     * @param event Event
     */
    void publish(Object event);

}
