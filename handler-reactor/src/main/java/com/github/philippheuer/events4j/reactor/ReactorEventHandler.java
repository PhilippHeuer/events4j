package com.github.philippheuer.events4j.reactor;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.reactor.util.Events4JSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.scheduler.forkjoin.ForkJoinPoolScheduler;

import java.util.function.Consumer;

@Slf4j
public class ReactorEventHandler implements IEventHandler {

    /**
     * The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     */
    @Getter
    private final Scheduler scheduler;

    /**
     * Used to bridge gateway events to the subscribers
     */
    @Getter
    private final FluxProcessor<Object, Object> processor;

    /**
     * Event Sink
     */
    @Getter
    private final FluxSink<Object> eventSink;

    /**
     * Creates a new ReactorEventHandler
     */
    public ReactorEventHandler() {
        this.scheduler = ForkJoinPoolScheduler.create("events4j-events", Math.max(Runtime.getRuntime().availableProcessors(), 4));
        this.processor = EmitterProcessor.create(8192, true);
        this.eventSink = processor.sink(FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * Constructor to provide a custom processor / scheduler to the ReactorEventHandler
     *
     * @param scheduler        The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     * @param processor        Used to bridge gateway events to the subscribers
     * @param overflowStrategy Safely gates a multi-threaded producer.
     */
    public ReactorEventHandler(Scheduler scheduler, FluxProcessor<Object, Object> processor, FluxSink.OverflowStrategy overflowStrategy) {
        this.scheduler = scheduler;
        this.processor = processor;
        this.eventSink = processor.sink(overflowStrategy);
    }

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void publish(Object event) {
        // publish event
        eventSink.next(event);
    }

    /**
     * Retrieves a {@link reactor.core.publisher.Flux} of the given event type. Also makes sure that the subscriber only gets one event at a time, unless specified otherwise.
     *
     * @param eventClass the event class to obtain events from
     * @param consumer   the event consumer / handler method
     * @param <E>        the event type
     * @return a new {@link reactor.core.publisher.Flux} of the given eventType
     */
    public <E extends Object> IDisposable onEvent(Class<E> eventClass, Consumer<E> consumer) {
        Flux<E> flux = processor
                .publishOn(this.scheduler)
                .ofType(eventClass);

        Subscriber<E> subscription = new Events4JSubscriber(consumer);
        flux.subscribe(subscription);

        return (IDisposable) subscription;
    }

    /**
     * Shutdown
     */
    public void close() {
        // complete sink
        eventSink.complete();

        // delayed
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // dispose scheduler
        if (!scheduler.isDisposed()) {
            scheduler.dispose();
        }
    }
}
