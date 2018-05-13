package com.github.philippheuer.events4j.domain;

import com.github.philippheuer.events4j.services.ServiceMediator;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

/**
 * Used to represent an event.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Setter
public abstract class Event {

    /**
     * Unique event id
     */
    private String eventId;

    /**
     * Event fired at
     */
    private Calendar firedAt;

    /**
     * Holds a reference to the ServiceMediator to access 3rd party services
     */
    private ServiceMediator serviceMediator;

    /**
     * Constructor
     */
    public Event() {
        this.firedAt = Calendar.getInstance();
    }
}
