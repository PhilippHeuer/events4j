package com.github.philippheuer.events4j.reactor;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Scheduler;
import reactor.scheduler.forkjoin.ForkJoinPoolScheduler;
import reactor.util.concurrent.WaitStrategy;

import java.util.ArrayList;
import java.util.List;
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
    private final FluxProcessor<IEvent, IEvent> processor;

    /**
     * Event Sink
     */
    @Getter
    private final FluxSink<IEvent> eventSink;

    /**
     * Active Subscriptions
     */
    @Getter
    private final List<Disposable> subscriptions = new ArrayList<>();

    /**
     * Creates a new ReactorEventHandler
     */
    public ReactorEventHandler() {
        this.scheduler = ForkJoinPoolScheduler.create("events4j-events", Runtime.getRuntime().availableProcessors() > 4 ? Runtime.getRuntime().availableProcessors() : 4);
        this.processor = TopicProcessor.<IEvent>builder()
                .name("events4j-processor")
                .waitStrategy(WaitStrategy.sleeping())
                .bufferSize(8192)
                .build();
        this.eventSink = processor.sink(FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * Constructor to provide a custom processor / scheduler to the ReactorEventHandler
     *
     * @param scheduler        The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     * @param processor        Used to bridge gateway events to the subscribers
     * @param overflowStrategy Safely gates a multi-threaded producer.
     */
    public ReactorEventHandler(Scheduler scheduler, FluxProcessor<IEvent, IEvent> processor, FluxSink.OverflowStrategy overflowStrategy) {
        this.scheduler = scheduler;
        this.processor = processor;
        this.eventSink = processor.sink(overflowStrategy);
    }

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void publish(IEvent event) {
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
    public <E extends IEvent> Disposable onEvent(Class<E> eventClass, Consumer<E> consumer) {
        Flux<E> flux = processor
                .publishOn(this.scheduler)
                .ofType(eventClass)
                .limitRequest(15);
        Disposable disposable = flux.subscribe(consumer);

        return disposable;
    }

    /**
     * Shutdown
     */
    public void close() {
        // cancel all subscriptions
        subscriptions.forEach(Disposable::dispose);

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
