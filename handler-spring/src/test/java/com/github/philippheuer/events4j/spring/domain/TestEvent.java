package com.github.philippheuer.events4j.spring.domain;

import com.github.philippheuer.events4j.core.domain.Event;

public class TestEvent extends Event {

    public static Integer count = 0;

    public TestEvent() {
        super();
        count = count + 1;
        this.setEventId(count.toString());
    }

}
