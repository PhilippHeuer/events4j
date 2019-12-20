package com.github.philippheuer.events4j.api.service;

import com.github.philippheuer.events4j.api.domain.IEvent;

public interface IEventHandler extends AutoCloseable {

    /**
     * Publish a Event
     *
     * @param event Event
     */
    void publish(IEvent event);

}
