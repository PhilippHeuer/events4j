package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.service.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SpringEventHandler implements IEventHandler {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void publish(Object event) {
        // publish event
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Shutdown
     */
    public void close() {
        // nothing
    }

}
