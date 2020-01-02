package com.github.philippheuer.events4j.simple.domain;

import com.github.philippheuer.events4j.core.domain.Event;

/**
 * A test-event to use when testing events4j.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
public class TestEvent extends Event {

    public static Integer count = 0;

    public TestEvent() {
        super();
        count = count + 1;
        this.setEventId(count.toString());
    }

}
