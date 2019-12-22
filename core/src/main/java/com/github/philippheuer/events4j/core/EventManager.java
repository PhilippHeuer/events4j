package com.github.philippheuer.events4j.core;

import com.github.philippheuer.events4j.annotation.AnnotationEventHandler;
import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import com.github.philippheuer.events4j.core.services.ServiceMediator;
import com.github.philippheuer.events4j.reactor.ReactorEventHandler;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The EventManager
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Slf4j
public class EventManager implements IEventManager {

    /**
     * Registry
     */
    private final MeterRegistry metricsRegistry = Metrics.globalRegistry;

    /**
     * Holds the ServiceMediator
     */
    private final IServiceMediator serviceMediator;

    /**
     * Event Handlers
     */
    private ArrayList<IEventHandler> eventHandlers = new ArrayList<>();

    /**
     * Annotation based event manager state
     */
    private boolean annotationEventManagerState = false;

    /**
     * is Stopped?
     */
    private boolean isStopped = false;

    /**
     * Constructor
     */
    public EventManager() {
        this.serviceMediator = new ServiceMediator(this);
    }

    /**
     * Registers a EventHandler
     *
     * @param eventHandler IEventHandler
     */
    public void registerEventHandler(IEventHandler eventHandler) {
        if (!eventHandlers.contains(eventHandler)) {
            eventHandlers.add(eventHandler);
        }
    }

    /**
     * Automatic discovery of optional components
     */
    public void autoDiscovery() {
        // Annotation
        try {
            // check if class is present
            Class.forName("com.github.philippheuer.events4j.annotation.AnnotationEventHandler");

            log.info("Auto Discovery: AnnotationEventHandler registered!");
            AnnotationEventHandler annotationEventHandler = new AnnotationEventHandler();
            registerEventHandler(annotationEventHandler);
        } catch (ClassNotFoundException ex) {
            log.debug("Auto Discovery: AnnotationEventHandler not available!");
        }

        // Reactor
        try {
            // check if class is present
            Class.forName("com.github.philippheuer.events4j.reactor.ReactorEventHandler");

            log.info("Auto Discovery: ReactorEventHandler registered!");
            ReactorEventHandler reactorEventHandler = new ReactorEventHandler();
            registerEventHandler(reactorEventHandler);
        } catch (ClassNotFoundException ex) {
            log.debug("Auto Discovery: ReactorEventHandler not available!");
        }
    }

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void publish(IEvent event) {
        // metrics
        metricsRegistry.counter("events4j.process", "category", event.getClass().getSuperclass() != null ? event.getClass().getSuperclass().getSimpleName() : null, "name", event.getClass().getSimpleName()).increment();

        // inject serviceMediator
        if (event.getServiceMediator() == null) {
            event.setServiceMediator(getServiceMediator());
        }

        // log event dispatch
        if (log.isDebugEnabled()) {
            log.debug("Dispatching event of type {} with id {}.", event.getClass().getSimpleName(), event.getEventId());
        }

        // check for stop
        if (isStopped) {
            log.error("Event scheduler stopped, dropped event [{}]!", event.getEventId());
        }

        // dispatch event to each handler
        eventHandlers.forEach(eventHandler -> eventHandler.publish(event));
    }

    /**
     * Checks if a fiven eventHandler is registered / present
     *
     * @param eventHandlerClass the event handler class
     * @return boolean
     */
    public boolean hasEventHandler(Class eventHandlerClass) {
        Optional<IEventHandler> eventHandler = getEventHandlers().stream().filter(h -> h.getClass().getName().equalsIgnoreCase(eventHandlerClass.getName())).findFirst();
        return eventHandler.isPresent();
    }

    /**
     * Retrieves a EventHandler of the provided type
     *
     * @param eventHandlerClass the event handler class
     * @param <E> the eventHandler type
     * @return a reference to the requested event handler
     */
    public <E> E getEventHandler(Class<E> eventHandlerClass) {
        Optional<E> eventHandler = getEventHandlers().stream().filter(h -> h.getClass().getName().equalsIgnoreCase(eventHandlerClass.getName())).map(h -> (E) h).findFirst();
        if (eventHandler.isPresent()) {
            return eventHandler.get();
        }

        throw new RuntimeException("No eventHandler of type " + eventHandlerClass.getName() + " is registered!");
    }

    /**
     * Shutdown
     */
    public void close() {
        isStopped = true;
        eventHandlers.forEach(eventHandler -> {
            try {
                eventHandler.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
