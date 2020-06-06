package com.github.philippheuer.events4j.simple.listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.philippheuer.events4j.simple.domain.TestEvent;

public class TestEventHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestEventHandler.class);

    public int eventsProcessed = 0;

    /**
     * TestEvent Listener
     *
     * @param event TestEvent
     */
    @EventSubscriber
    public void onTestEvent(TestEvent event) {
        log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getInstant().toString());
        eventsProcessed++;
    }

}
