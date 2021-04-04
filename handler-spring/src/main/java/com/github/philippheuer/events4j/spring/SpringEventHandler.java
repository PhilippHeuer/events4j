package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Spring Event Handler
 */
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

    @Override
    public <E> IDisposable onEvent(Class<E> eventClass, Consumer<E> consumer) {
        throw new RuntimeException("SpringEventHandler does not support onEvent, please consumer events as spring application events!");
    }

    /**
     * Shutdown
     */
    public void close() {
        // nothing
    }

}
