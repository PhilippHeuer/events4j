package com.github.philippheuer.events4j.simple.listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.philippheuer.events4j.simple.domain.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEventHandler {

    private static final Logger log = LoggerFactory.getLogger(TestEventHandler.class);

    public int eventsProcessed;

    /**
     * TestEvent Listener
     *
     * @param event TestEvent
     */
    @EventSubscriber
    private void onTestEvent(TestEvent event) {
        log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAtInstant().toString());
        eventsProcessed++;
    }

}
