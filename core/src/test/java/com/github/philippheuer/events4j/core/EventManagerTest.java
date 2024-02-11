package com.github.philippheuer.events4j.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.core.domain.TestEventObject;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
class EventManagerTest {

    private static EventManager eventManager;

    @BeforeAll
    static void initializeEventManager() {
        eventManager = new EventManager();
        eventManager.autoDiscovery();
        eventManager.setDefaultEventHandler(SimpleEventHandler.class);
    }

    @Test
    void autoDiscovery() {
        assertEquals(2, eventManager.getEventHandlers().size(), "should have discovered 2 handlers");
    }

    @Test
    void getEventHandlerByClass() {
        SimpleEventHandler eventHandler = eventManager.getEventHandler(SimpleEventHandler.class);
        assertNotNull(eventHandler, "should fine a eventHandler for class SimpleEventHandler");
    }

    @Test
    void hasEventHandlerByClass() {
        assertTrue(eventManager.hasEventHandler(SimpleEventHandler.class), "should fine a eventHandler for class SimpleEventHandler");
    }

    @Test
    void uniqueOnEvent() {
        // Register Listener
        IDisposable disposableA = eventManager.onEvent("test", TestEventObject.class, System.out::println);
        IDisposable disposableB = eventManager.onEvent("test", TestEventObject.class, System.out::println);

        // Verify
        assertEquals(1, eventManager.getActiveSubscriptions().size());
        assertNotNull(disposableA);
        assertNull(disposableB);
    }

    @AfterAll
    static void shutdownEventManager() {
        eventManager.close();
    }

}
