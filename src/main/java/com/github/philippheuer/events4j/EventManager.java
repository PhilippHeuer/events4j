package com.github.philippheuer.events4j;

import com.github.philippheuer.events4j.domain.Event;
import com.github.philippheuer.events4j.services.ServiceMediator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Calendar;
import java.util.UUID;

/**
 * The EventManager
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Slf4j
public class EventManager {

    /**
     * The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     */
    private final Scheduler scheduler;

    /**
     * Used to bridge gateway events to the subscribers
     */
    private final FluxProcessor<Event, Event> processor;

    /**
     * Holds the ServiceMediator
     */
    private final ServiceMediator serviceMediator;

    /**
     * Creates a new EventManager
     */
    public EventManager() {
        this.scheduler = Schedulers.parallel();
        this.processor = EmitterProcessor.create(256);
        this.serviceMediator = new ServiceMediator(this);
    }

    /**
     * Constructor to provide a custom processor / scheduler
     */
    public EventManager(Scheduler scheduler, FluxProcessor<Event, Event> processor) {
        this.scheduler = scheduler;
        this.processor = processor;
        this.serviceMediator = new ServiceMediator(this);
    }

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void dispatchEvent(Event event) {
        // enrich event data
        // - fired at
        event.setFiredAt(Calendar.getInstance());
        // - serviceMediator to access 3rd party services
        event.setServiceMediator(getServiceMediator());
        // - unique event id
        event.setEventId(UUID.randomUUID().toString());

        // log event dispatch
        log.debug("Dispatching event of type {} with id {}.", event.getClass().getSimpleName(), event.getEventId());

        // publish event
        Mono<Event> monoEvent = Mono.just(event);
        monoEvent.subscribeWith(processor).subscribe();
    }

    /**
     * Retrieves a {@link reactor.core.publisher.Flux} of the given event type.
     *
     * @param eventClass the event class to obtain events from
     * @param <E> the eventType
     * @return a new {@link reactor.core.publisher.Flux} of the given eventType
     */
    public <E extends Event> Flux<E> onEvent(Class<E> eventClass) {
        return processor.publishOn(scheduler).ofType(eventClass);
    }

}
