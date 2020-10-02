package com.github.philippheuer.events4j.simple;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.domain.TestEvent;
import com.github.philippheuer.events4j.simple.domain.TestEventObject;
import com.github.philippheuer.events4j.simple.listener.TestEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class SimpleEventHandlerTest {

    private static EventManager eventManager;

    private static int eventsHandled = 0;

    @BeforeAll
    public static void beforeAll() {
        eventManager = new EventManager();
        SimpleEventHandler simpleEventHandler = new SimpleEventHandler();
        eventManager.setDefaultEventHandler(SimpleEventHandler.class);
        eventManager.registerEventHandler(simpleEventHandler);
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testObjectEvent() throws Exception {
        eventsHandled = 0;

        // Consumer based handler
        IDisposable disposable = eventManager.onEvent(TestEventObject.class, testEvent -> {
            eventsHandled = eventsHandled + 1;
        });

        // Dispatch
        eventManager.publish(new TestEventObject());

        // Dispose Handler
        disposable.dispose();

        // Verify
        Assertions.assertEquals(1, eventsHandled, "one event should have been handled");
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testConsumerHandler() throws Exception {
        eventsHandled = 0;

        // Consumer based handler
        IDisposable disposable = eventManager.onEvent(TestEvent.class, testEvent -> {
            eventsHandled = eventsHandled + 1;
        });

        // Dispatch
        eventManager.publish(new TestEvent());

        // Dispose Handler
        disposable.dispose();

        // Dispatch another event
        eventManager.publish(new TestEvent());

        // Verify
        Assertions.assertEquals(1, eventsHandled, "only one event should have been handled, since we disposed the handler after the first publish call");
    }

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testAnnotationHandler() throws Exception {
        // Register Listener
        TestEventHandler testEventHandler = new TestEventHandler();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(testEventHandler);

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.publish(testEvent);

        // Verify
        Assertions.assertEquals(1, testEventHandler.eventsProcessed, "check that one event was processed");
    }

    @AfterAll
    public static void afterAll() throws Exception {
        // Shutdown
        eventManager.close();
    }

}
