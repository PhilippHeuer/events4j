package com.github.philippheuer.events4j.reactor;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.reactor.domain.TestEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;

/**
 * Reactor EventHandler Test
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
public class ReactorEventHandlerTest {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ReactorEventHandlerTest.class);

    private static IEventManager eventManager;

    private static int eventsProcessed = 0;

    @BeforeAll
    public static void beforeAll() {
        eventManager = new EventManager();
        ReactorEventHandler reactorEventHandler = new ReactorEventHandler();
        eventManager.registerEventHandler(reactorEventHandler);
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testReactorEventHandlerWithTestEvent() throws Exception {
        // Register Listener
        Disposable disposable = eventManager.getEventHandler(ReactorEventHandler.class).onEvent(TestEvent.class, event -> {
            log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getInstant().toString());
            eventsProcessed = eventsProcessed + 1;
        });

        // dispatch
        eventManager.publish(new TestEvent());
        Thread.sleep(1000);

        // dispose handler and dispatch 1 more event
        disposable.dispose();
        eventManager.publish(new TestEvent());
        Thread.sleep(1000);

        // Verify
        Assertions.assertEquals(1, eventsProcessed, "only one event should have been handled, since we disposed the handler after the first publish call");
    }

    @AfterAll
    public static void afterAll() throws Exception {
        // shutdown
        eventManager.close();
    }

}
