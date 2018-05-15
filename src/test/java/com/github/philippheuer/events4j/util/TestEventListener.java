package com.github.philippheuer.events4j.util;

import com.github.philippheuer.events4j.annotation.EventSubscriber;
import com.github.philippheuer.events4j.domain.TestEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEventListener {

    @EventSubscriber
    public void onTestEvent(TestEvent testEvent) {
        log.info("TestEvent Listener Executed.");
    }

}
