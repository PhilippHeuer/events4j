package com.github.philippheuer.events4j.api.domain;

import com.github.philippheuer.events4j.api.service.IServiceMediator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public interface IEvent {

    /**
     * Delete multiple items from the list.
     *
     * @deprecated Replaced by {@link #setFiredAtInstant(Instant)} ()}
     */
    @Deprecated
    void setFiredAt(Calendar calendar);

    default void setFiredAtInstant(Instant instant) {
        setFiredAt(GregorianCalendar.from(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())));
    }

    /**
     * Delete multiple items from the list.
     *
     * @deprecated Replaced by {@link #getFiredAtInstant()} ()}
     */
    @Deprecated
    Calendar getFiredAt();

    default Instant getFiredAtInstant() {
        return getFiredAt().toInstant();
    }

    void setEventId(String id);

    String getEventId();

    void setServiceMediator(IServiceMediator serviceMediator);

    IServiceMediator getServiceMediator();

}
