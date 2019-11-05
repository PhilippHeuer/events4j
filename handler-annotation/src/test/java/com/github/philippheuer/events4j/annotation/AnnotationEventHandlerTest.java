package com.github.philippheuer.events4j.annotation;

import com.github.philippheuer.events4j.annotation.domain.TestEvent;
import com.github.philippheuer.events4j.annotation.listener.TestEventHandler;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.api.IEventManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class AnnotationEventHandlerTest {

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testAnnotationWithTestEvent() {
        IEventManager eventManager = new EventManager();
        AnnotationEventHandler annotationEventHandler = new AnnotationEventHandler();
        eventManager.registerEventHandler(annotationEventHandler);

        // Register Listener
        TestEventHandler testEventHandler = new TestEventHandler();
        annotationEventHandler.registerListener(testEventHandler);

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.publish(testEvent);

        // Verify
        Assertions.assertEquals(1, testEventHandler.eventsProcessed, "check that one event was processed");

        // Shutdown
        eventManager.shutdown();
    }

}
