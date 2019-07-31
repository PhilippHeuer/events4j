package com.github.philippheuer.events4j;

import com.github.philippheuer.events4j.domain.TestEvent;
import com.github.philippheuer.events4j.util.TestEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.TopicProcessor;
import reactor.core.scheduler.Schedulers;

/**
 * EventManager Unit Tests
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
public class EventManagerTest {

    /**
     * Initialize a EventManager Instance
     */
    private EventManager getEventManager() {
        return new EventManager(
                Schedulers.newParallel("events4j-scheduler", Runtime.getRuntime().availableProcessors() * 2),
                TopicProcessor.create("events4j-processor", 16),
                FluxSink.OverflowStrategy.BUFFER);
    }

    /**
     * Wait until all events are processed before running the next test
     */
    private void waitUntilAllEventsAreProcessed(EventManager eventManager) {
        // wait until all events have been processed
        while (((TopicProcessor) eventManager.getProcessor()).getPending() > 0) {
            try {
                Thread.sleep(1000);
                log.debug("Event queue not empty [{}], waiting ...", ((TopicProcessor) eventManager.getProcessor()).getPending());
            } catch (Exception ex) {
                // nothing
            }
        }
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    @Disabled
    public void testOnEventConsumer() {
        EventManager eventManager = getEventManager();

        // Consumer
        eventManager.onEvent(TestEvent.class).subscribe(event -> {
            log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAt().toInstant().toString());
        });

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.dispatchEvent(testEvent);
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    @Disabled
    public void testMethodAnnotationEventConsumer() {
        EventManager eventManager = getEventManager();

        // Register Consumer
        eventManager.registerListener(new TestEventListener());

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.dispatchEvent(testEvent);
    }

}
