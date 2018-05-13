package com.github.philippheuer.events4j.util;

import com.github.philippheuer.events4j.EventManager;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.scheduler.Schedulers;

/**
 * Provides EventManger Test Utility Functions
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
public class EventManagerTestUtil {

    /**
     * Returns a initialized EventManager that processes events immediately
     *
     * @return EventManager
     */
    static public EventManager getEventManager() {
        return new EventManager(Schedulers.immediate(), EmitterProcessor.create(256));
    }

}
