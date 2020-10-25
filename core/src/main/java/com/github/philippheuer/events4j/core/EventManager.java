package com.github.philippheuer.events4j.core;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import com.github.philippheuer.events4j.core.services.ServiceMediator;
import com.github.philippheuer.events4j.reactor.ReactorEventHandler;
import com.github.philippheuer.events4j.reactor.util.ReactorDisposableWrapper;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.SimpleDisposableWrapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final List<IEventHandler> eventHandlers = new ArrayList<>();

    /**
     * is Stopped?
     */
    private boolean isStopped = false;

    /**
     * Default EventHandler
     */
    private String defaultEventHandler;

    /**
     * Holds all active subscriptions
     */
    private final Map<String, IEventSubscription> activeSubscriptions = Collections.synchronizedMap(new HashMap<>());

    /**
     * Holds the current sequence for consumer registrations
     */
    private final AtomicInteger consumerSequence = new AtomicInteger(1);

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
     * @return returns the list of all active subscriptions
     */
    public List<IEventSubscription> getActiveSubscriptions() {
        return Collections.unmodifiableList(new ArrayList<>(activeSubscriptions.values()));
    }

    /**
     * Automatic discovery of optional components
     */
    public void autoDiscovery() {
        // Annotation
        try {
            // check if class is present
            Class.forName("com.github.philippheuer.events4j.simple.SimpleEventHandler");

            log.info("Auto Discovery: SimpleEventHandler registered!");
            SimpleEventHandler simpleEventHandler = new SimpleEventHandler();
            registerEventHandler(simpleEventHandler);
        } catch (ClassNotFoundException ex) {
            log.debug("Auto Discovery: SimpleEventHandler not available!");
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
     * @param event A event of any kinds, should implement IEvent if possible
     */
    public void publish(Object event) {
        // check for stop
        if (isStopped) {
            log.warn("Tried to dispatch a event to a closed eventManager!");
            return;
        }

        // metrics
        metricsRegistry.counter("events4j.published", "name", event.getClass().getSimpleName()).increment();

        // implements IEvent?
        if (event instanceof IEvent) {
            // inject serviceMediator
            IEvent iEvent = (IEvent) event;

            if (iEvent.getServiceMediator() == null) {
                iEvent.setServiceMediator(getServiceMediator());
            }

            // log event dispatch
            if (log.isDebugEnabled()) {
                log.debug("Dispatching event of type {} with id {}.", event.getClass().getSimpleName(), iEvent.getEventId());
            }
        } else {
            // log event dispatch
            if (log.isDebugEnabled()) {
                log.debug("Dispatching event of type {}.", event.getClass().getSimpleName());
            }
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
     * @param <E>               the eventHandler type
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
     * Registers a new consumer based default event handler if supported
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new Disposable of the given eventType
     */
    public <E> IEventSubscription onEvent(Class<E> eventClass, Consumer<E> consumer) {
        return onEvent(consumer.getClass().getCanonicalName(), eventClass, consumer);
    }

    /**
     * Registers a named consumer based default event handler if supported
     *
     * @param id         unique name or id for this consumer
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return           a new Disposable of the given eventType
     */
    public <E> IEventSubscription onEvent(String id, Class<E> eventClass, Consumer<E> consumer) {
        return onEvent(id, eventClass, consumer, false);
    }

    /**
     * Registers a named consumer if no consumer with the same name is registered yet
     *
     * @param id         unique name or id for this consumer
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return           a new Disposable of the given eventType, null if a consumer for the given id was already registered
     */
    public <E> IEventSubscription onEventIfIdUnique(String id, Class<E> eventClass, Consumer<E> consumer) {
        return onEvent(id, eventClass, consumer, true);
    }

    /**
     * Registers a named consumer based default event handler if supported
     *
     * @param id         unique name or id for this consumer
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @param idUnique   enforce that every unique id can only be registered on one active subscription?
     * @return           a new Disposable of the given eventType
     */
    private <E> IEventSubscription onEvent(String id, Class<E> eventClass, Consumer<E> consumer, boolean idUnique) {
        // return null if a disposable with the given id is already present when idUnique is set
        if (idUnique && activeSubscriptions.containsKey(id)) {
            return null;
        } else if (activeSubscriptions.containsKey(id)) {
            // register a id that is already present, but no unique constraint
            id = id + "/" + consumerSequence.getAndAdd(1);
        }

        String eventHandler = defaultEventHandler;
        if (eventHandler == null) {
            if (eventHandlers.size() == 1) {
                eventHandler = eventHandlers.get(0).getClass().getCanonicalName();
            } else {
                throw new RuntimeException("When more than one eventHandler has been registered you have to specify the defaultEventHandler using EventManager#setDefaultEventHandler!");
            }
        }

        if ("com.github.philippheuer.events4j.simple.SimpleEventHandler".equalsIgnoreCase(eventHandler)) {
            IDisposable disposable = getEventHandler(SimpleEventHandler.class).onEvent(eventClass, consumer);
            return new SimpleDisposableWrapper(disposable, id, eventClass, consumer, activeSubscriptions);
        } else if ("com.github.philippheuer.events4j.reactor.ReactorEventHandler".equalsIgnoreCase(eventHandler)) {
            Disposable disposable = getEventHandler(ReactorEventHandler.class).onEvent(eventClass, consumer);
            return new ReactorDisposableWrapper(disposable, id, eventClass, consumer, activeSubscriptions);
        }

        throw new RuntimeException("EventHandler " + eventHandler + " does not support EventManager.onEvent!");
    }

    /**
     * Sets the default eventHandler
     *
     * @param eventHandler eventHandler
     */
    @SuppressWarnings("rawtypes")
    public void setDefaultEventHandler(Class eventHandler) {
        this.defaultEventHandler = eventHandler.getCanonicalName();
    }

    /**
     * Sets the default eventHandler
     *
     * @param eventHandler canonical name of the eventHandler class
     */
    public void setDefaultEventHandler(String eventHandler) {
        this.defaultEventHandler = eventHandler;
    }

    /**
     * Shutdown
     */
    public void close() {
        isStopped = true;

        // cancel subscriptions
        getActiveSubscriptions().forEach(IDisposable::dispose);

        // close eventhandlers
        eventHandlers.forEach(eventHandler -> {
            try {
                eventHandler.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
