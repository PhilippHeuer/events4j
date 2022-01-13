package com.github.philippheuer.events4j.reactor;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.api.service.IEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.philippheuer.events4j.reactor.domain.TestEvent;
import com.github.philippheuer.events4j.reactor.domain.TestEventObject;
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

    private static EventManager eventManager;

    private static int eventsProcessed = 0;

    private static final Class<? extends IEventHandler> REACTOR_EVENTHANDLER = ReactorEventHandler.class;

    @BeforeAll
    public static void beforeAll() {
        eventManager = new EventManager();
        ReactorEventHandler reactorEventHandler = new ReactorEventHandler();
        eventManager.registerEventHandler(reactorEventHandler);
        eventManager.setDefaultEventHandler(REACTOR_EVENTHANDLER);
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testReactorEventHandlerWithTestEventObject() throws Exception {
        // Register Listener
        IDisposable disposable = eventManager.onEvent(TestEventObject.class, event -> {
            log.info("Received a event.");
            eventsProcessed = eventsProcessed + 1;
        });
        Assertions.assertEquals(1, eventManager.getActiveSubscriptions().size());

        // dispatch
        eventManager.publish(new TestEventObject());
        Thread.sleep(1000);

        // dispose handler and dispatch 1 more event
        disposable.dispose();

        // Verify
        Assertions.assertEquals(0, eventManager.getActiveSubscriptions().size());
        Assertions.assertEquals(1, eventsProcessed, "one event should have been handled");
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testReactorEventHandlerWithTestEvent() throws Exception {
        // Register Listener
        IDisposable disposable = eventManager.getEventHandler(REACTOR_EVENTHANDLER).onEvent(Event.class, event -> {
            log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAtInstant().toString());
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
