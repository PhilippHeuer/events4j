package com.github.philippheuer.events4j;

import com.github.philippheuer.events4j.annotation.EventSubscriber;
import com.github.philippheuer.events4j.domain.Event;
import com.github.philippheuer.events4j.exception.EventBufferOverflowException;
import com.github.philippheuer.events4j.services.AnnotationEventManager;
import com.github.philippheuer.events4j.services.ServiceMediator;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.concurrent.WaitStrategy;

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
     * Registry
     */
    private final MeterRegistry metricsRegistry = Metrics.globalRegistry;

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
     * Event Sink
     */
    private final FluxSink<Event> eventSink;

    /**
     * Annotation based event manager
     */
    private final AnnotationEventManager annotationEventManager = new AnnotationEventManager();

    /**
     * Annotation based event manager state
     */
    private boolean annotationEventManagerState = false;

    /**
     * Creates a new EventManager
     */
    public EventManager() {
        this.scheduler = Schedulers.newParallel("events4j-scheduler", Runtime.getRuntime().availableProcessors() * 2);
        this.processor = TopicProcessor.<Event>builder()
                .name("events4j-processor")
                .waitStrategy(WaitStrategy.sleeping())
                .bufferSize(8192)
                .build();
        this.eventSink = processor.sink(FluxSink.OverflowStrategy.BUFFER); // will be handled in dispatchEvent
        this.serviceMediator = new ServiceMediator(this);
    }

    /**
     * Constructor to provide a custom processor / scheduler
     *
     * @param scheduler The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     * @param processor Used to bridge gateway events to the subscribers
     * @param overflowStrategy Safely gates a multi-threaded producer.
     */
    public EventManager(Scheduler scheduler, FluxProcessor<Event, Event> processor, FluxSink.OverflowStrategy overflowStrategy) {
        this.scheduler = scheduler;
        this.processor = processor;
        this.eventSink = processor.sink(overflowStrategy);
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

        // make sure we won't flood the subscribers
        long pendingEvents = ((TopicProcessor<Event>) this.processor).getPending();
        int bufferSize = this.processor.getBufferSize();
        if (pendingEvents >= bufferSize) {
            metricsRegistry.counter("events4j.drop", "category", event.getClass().getSuperclass() != null ? event.getClass().getSuperclass().getSimpleName() : null, "name", event.getClass().getSimpleName()).increment();
            log.error("{}/{} events in queue. New event {} was rejected!", pendingEvents, bufferSize, event.toString());
            throw new EventBufferOverflowException("Event Buffer Overflow, your processors can't keep up with generated events! A event has been dropped and will not be processed.");
        }

        // log event dispatch
        if (log.isDebugEnabled()) {
            log.debug("Dispatching event of type {} with id {}.", event.getClass().getSimpleName(), event.getEventId());
        }
        metricsRegistry.counter("events4j.process", "category", event.getClass().getSuperclass() != null ? event.getClass().getSuperclass().getSimpleName() : null, "name", event.getClass().getSimpleName()).increment();

        // publish event
        eventSink.next(event);
    }

    /**
     * Retrieves a {@link reactor.core.publisher.Flux} of the given event type. Also makes sure that the subscriber only gets one event at a time, unless specified otherwise.
     *
     * @param eventClass the event class to obtain events from
     * @param <E> the eventType
     * @return a new {@link reactor.core.publisher.Flux} of the given eventType
     */
    public <E extends Event> ParallelFlux<E> onEvent(Class<E> eventClass) {
        return processor
                .subscribeOn(this.scheduler)
                .publishOn(this.scheduler)
                .ofType(eventClass)
                .parallel()
                .runOn(this.scheduler);
    }

    /**
     * Registers a listener using {@link EventSubscriber} method annotations.
     *
     * @param eventListener The class instance containing methods annotated with {@link EventSubscriber}.
     */
    public void registerListener(Object eventListener) {
        getAnnotationEventManager().registerListener(eventListener);
    }

    /**
     * Register annotation based event dispatcher
     */
    public void enableAnnotationBasedEvents() {
        // Annotation-based EventListener
        if (!annotationEventManagerState) {
            onEvent(Event.class).subscribe(annotationEventManager::dispatch);
            annotationEventManagerState = true;
        } else {
            log.warn("Annotation-based event manager is already enabled!");
        }
    }

}
