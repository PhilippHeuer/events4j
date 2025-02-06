package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.spring.domain.SpringListenerSubscription;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * Spring Event Handler
 */
@Service
@Slf4j
public class SpringEventHandler implements IEventHandler {

    private final ApplicationEventMulticaster applicationEventMulticaster;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public SpringEventHandler(ApplicationEventMulticaster applicationEventMulticaster, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventMulticaster = applicationEventMulticaster;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(@NonNull Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    @NonNull
    @Override
    public <E> IDisposable onEvent(@NonNull Class<E> eventClass, @NonNull Consumer<E> consumer) {
        ApplicationListener<ApplicationEvent> listener = event -> {
            if (event instanceof PayloadApplicationEvent) {
                PayloadApplicationEvent pae = (PayloadApplicationEvent) event;

                if (eventClass.isInstance(pae.getPayload())) {
                    consumer.accept((E) pae.getPayload());
                }
            }
        };

        applicationEventMulticaster.addApplicationListener(listener);

        // Return an IDisposable that removes the listener when called
        return SpringListenerSubscription.of(applicationEventMulticaster, listener);
    }

    public void close() {
        // nothing
    }

}
