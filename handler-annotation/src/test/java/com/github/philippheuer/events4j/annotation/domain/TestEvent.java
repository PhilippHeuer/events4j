package com.github.philippheuer.events4j.annotation.domain;

import com.github.philippheuer.events4j.core.domain.Event;

/**
 * A test-event to use when testing events4j.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
public class TestEvent extends Event {

    public static Integer id = 0;

    public TestEvent() {
        super();
        id = id + 1;
        this.setEventId(id.toString());
    }

}
