package com.github.philippheuer.events4j;

import com.github.philippheuer.events4j.domain.TestEvent;
import com.github.philippheuer.events4j.util.EventManagerTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * EventManager Unit Tests
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Slf4j
public class EventManagerTest {

    /**
     * Tests if events can be dispatched
     */
    @Test
    public void testDispatchEvent() {
        // Prepare
        EventManager eventManager = EventManagerTestUtil.getEventManager();

        // Consumer
        eventManager.onEvent(TestEvent.class).subscribe(event -> {
            log.info("Received event [{}] that was fired at {}.", event.getEventId(), event.getFiredAt().toInstant().toString());
        });

        // Dispatch
        TestEvent testEvent = new TestEvent();
        eventManager.dispatchEvent(testEvent);
    }

}
