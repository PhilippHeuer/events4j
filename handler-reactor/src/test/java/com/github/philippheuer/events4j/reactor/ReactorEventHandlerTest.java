package com.github.philippheuer.events4j.reactor;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.reactor.domain.TestEvent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * Reactor EventHandler Test
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
public class ReactorEventHandlerTest {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReactorEventHandlerTest.class);

    private final Integer eventsProcessed = 0;

    /**
     * Tests if events can be dispatched
     */
    @Test
    @SneakyThrows
    public void testReactorEventHandlerWithTestEvent() {
        IEventManager eventManager = new EventManager();
        ReactorEventHandler reactorEventHandler = new ReactorEventHandler();
        eventManager.registerEventHandler(reactorEventHandler);

        // Register Listener
        boolean triggered = false;
        reactorEventHandler.onEvent(TestEvent.class, event -> {
            log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAt().toInstant().toString());
        });

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.publish(testEvent);

        // wait a moment
        Thread.sleep(5000);

        // shutdown
        eventManager.close();

    }

}
