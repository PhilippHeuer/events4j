package com.github.philippheuer.events4j.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class AutoDiscoveryTest {

    @Test
    public void testAnnotationModule() {
        EventManager eventManager = new EventManager();
        eventManager.autoDiscovery();

        Assertions.assertEquals(2, eventManager.getEventHandlers().size(), "should have discovered 2 handlers");

        eventManager.shutdown();
    }

}
