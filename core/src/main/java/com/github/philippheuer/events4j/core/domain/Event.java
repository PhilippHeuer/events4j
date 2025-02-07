package com.github.philippheuer.events4j.core.domain;

import com.github.philippheuer.events4j.api.domain.IEvent;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Used to represent an event.
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
    private Instant firedAtInstant;

    /**
     * Holds a reference to the ServiceMediator to access 3rd party services
     */
    private IServiceMediator serviceMediator;

    /**
     * Constructor
     */
    public Event() {
        this(UUID.randomUUID().toString(), Instant.now());
    }

    /**
     * Constructor
     *
     * @param eventId Unique event id
     * @param firedAt Timestamp of the event firing
     */
    public Event(String eventId, Instant firedAt) {
        this.eventId = eventId;
        this.firedAtInstant = firedAt;
    }

    @Override
    public Calendar getFiredAt() {
        return GregorianCalendar.from(ZonedDateTime.ofInstant(this.firedAtInstant, ZoneId.systemDefault()));
    }

    @Override
    public void setFiredAt(Calendar calendar) {
        setFiredAtInstant(calendar.toInstant());
    }
}
