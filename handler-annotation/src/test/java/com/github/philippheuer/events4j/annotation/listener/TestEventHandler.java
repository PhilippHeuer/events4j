package com.github.philippheuer.events4j.annotation.listener;

import com.github.philippheuer.events4j.annotation.domain.EventSubscriber;
import com.github.philippheuer.events4j.annotation.domain.TestEvent;

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
        log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAt().toInstant().toString());
        eventsProcessed++;
    }

}
