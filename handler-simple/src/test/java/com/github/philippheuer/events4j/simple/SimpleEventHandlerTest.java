package com.github.philippheuer.events4j.simple;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.core.domain.Event;
import com.github.philippheuer.events4j.simple.domain.TestEvent;
import com.github.philippheuer.events4j.simple.domain.TestEventObject;
import com.github.philippheuer.events4j.simple.listener.TestEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
class SimpleEventHandlerTest {

    private static EventManager eventManager;

    private static int eventsHandled;

    @BeforeAll
    static void beforeAll() {
        eventManager = new EventManager();
        SimpleEventHandler simpleEventHandler = new SimpleEventHandler();
        eventManager.setDefaultEventHandler(SimpleEventHandler.class);
        eventManager.registerEventHandler(simpleEventHandler);
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    void objectEvent() throws Exception {
        eventsHandled = 0;

        // Consumer based handler
        IDisposable disposable = eventManager.onEvent(TestEventObject.class, testEvent -> {
            eventsHandled = eventsHandled + 1;
        });
        assertEquals(1, eventManager.getActiveSubscriptions().size());

        // Dispatch
        eventManager.publish(new TestEventObject());

        // Dispose Handler
        disposable.dispose();

        // Verify
        assertEquals(0, eventManager.getActiveSubscriptions().size());
        assertEquals(1, eventsHandled, "one event should have been handled");
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    void consumerHandler() throws Exception {
        eventsHandled = 0;

        // Consumer based handler
        IDisposable disposable = eventManager.onEvent(Event.class, testEvent -> {
            eventsHandled = eventsHandled + 1;
        });

        // Dispatch
        eventManager.publish(new TestEvent());

        // Dispose Handler
        disposable.dispose();

        // Dispatch another event
        eventManager.publish(new TestEvent());

        // Verify
        assertEquals(1, eventsHandled, "only one event should have been handled, since we disposed the handler after the first publish call");
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    void annotationHandler() throws Exception {
        // Register Listener
        TestEventHandler testEventHandler = new TestEventHandler();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(testEventHandler);

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.publish(testEvent);

        // Verify
        assertEquals(1, testEventHandler.eventsProcessed, "check that one event was processed");
    }

    @AfterAll
    static void afterAll() throws Exception {
        // Shutdown
        eventManager.close();
    }

}
