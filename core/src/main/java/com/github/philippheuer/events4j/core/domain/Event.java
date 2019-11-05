package com.github.philippheuer.events4j.core.domain;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import lombok.Data;
import lombok.Setter;

import java.util.Calendar;
import java.util.UUID;

/**
 * Used to represent an event.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public abstract class Event implements IEvent {

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
    @Setter
    private IServiceMediator serviceMediator;

    /**
     * Constructor
     */
    public Event() {
        eventId = UUID.randomUUID().toString();
        firedAt = Calendar.getInstance();
    }
}
