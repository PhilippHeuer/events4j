package com.github.philippheuer.events4j.core;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.api.domain.DisposableWrapper;
import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import com.github.philippheuer.events4j.core.services.ServiceMediator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private volatile boolean isStopped = false;

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
     * EventHandler Class Cache
     */
    private final Map<String, Class<? extends IEventHandler>> eventHandlerCache = new HashMap<>();

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
            eventHandlerCache.put(eventHandler.getClass().getCanonicalName(), eventHandler.getClass());
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
    @SuppressWarnings("unchecked")
    public void autoDiscovery() {
        // Annotation
        try {
            // check if class is present
            Class<? extends IEventHandler> handlerClass = (Class<? extends IEventHandler>) Class.forName("com.github.philippheuer.events4j.simple.SimpleEventHandler");

            log.info("Auto Discovery: SimpleEventHandler registered!");
            registerEventHandler(handlerClass.getDeclaredConstructor(new Class[0]).newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            log.debug("Auto Discovery: SimpleEventHandler not available!");
        }

        // Reactor
        try {
            // check if class is present
            Class<? extends IEventHandler> handlerClass = (Class<? extends IEventHandler>) Class.forName("com.github.philippheuer.events4j.reactor.ReactorEventHandler");

            log.info("Auto Discovery: ReactorEventHandler registered!");
            registerEventHandler(handlerClass.getDeclaredConstructor(new Class[0]).newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
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
     * Checks if a given eventHandler is registered / present
     *
     * @param eventHandlerClass the event handler class
     * @return boolean
     */
    public boolean hasEventHandler(Class<? extends IEventHandler> eventHandlerClass) {
        return getEventHandlers().stream().anyMatch(h -> h.getClass().getName().equalsIgnoreCase(eventHandlerClass.getName()));
    }

    /**
     * Retrieves a EventHandler of the provided type
     *
     * @param eventHandlerClass the event handler class
     * @param <E>               the eventHandler type
     * @return a reference to the requested event handler
     */
    public <E extends IEventHandler> E getEventHandler(Class<E> eventHandlerClass) {
        Optional<E> eventHandler = getEventHandlers().stream().filter(h -> h.getClass().getName().equalsIgnoreCase(eventHandlerClass.getName())).map(h -> (E) h).findAny();
        return eventHandler.orElseThrow(() -> new RuntimeException("No eventHandler of type " + eventHandlerClass.getName() + " is registered!"));
    }

    /**
     * Registers a event consumer
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new Disposable of the given eventType
     */
    public <E> IEventSubscription onEvent(Class<E> eventClass, Consumer<E> consumer) {
        return onEvent(consumer.getClass().getCanonicalName()+"/"+consumerSequence.getAndAdd(1), eventClass, consumer);
    }

    /**
     * Registers a event consumer and assigns a id
     * <p>
     * This method will return null if the id was already used.
     *
     * @param id         unique id used to identify this consumer
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return           a new Disposable of the given eventType
     */
    public synchronized <E> IEventSubscription onEvent(String id, Class<E> eventClass, Consumer<E> consumer) {
        // return null if a disposable with the given id is already present when idUnique is set
        if (activeSubscriptions.containsKey(id)) {
            return null;
        }

        // use default event handler (or automatically set default event handler if exactly one handler has been registered)
        String eventHandler = defaultEventHandler;
        if (eventHandler == null) {
            if (eventHandlers.size() == 1) {
                defaultEventHandler = eventHandlers.get(0).getClass().getCanonicalName();
                eventHandler = defaultEventHandler;
            } else {
                throw new RuntimeException("When more than one eventHandler has been registered you have to specify the defaultEventHandler using EventManager#setDefaultEventHandler!");
            }
        }

        // return wrapped disposable
        Class<? extends IEventHandler> handlerClass = eventHandlerCache.get(eventHandler);
        if (handlerClass != null) {
            IDisposable disposable = getEventHandler(handlerClass).onEvent(eventClass, consumer);
            if (disposable != null) {
                return new DisposableWrapper(disposable, id, eventClass, consumer, activeSubscriptions);
            }
        }

        throw new RuntimeException("EventHandler " + eventHandler + " has not been registered for EventManager.onEvent!");
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
