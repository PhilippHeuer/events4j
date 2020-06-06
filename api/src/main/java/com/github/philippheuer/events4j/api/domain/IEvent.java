package com.github.philippheuer.events4j.api.domain;

import com.github.philippheuer.events4j.api.service.IServiceMediator;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public interface IEvent {

    @Deprecated
    void setFiredAt(Calendar calendar);

    default void setInstant(Instant instant) {
        setFiredAt(GregorianCalendar.from(ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())));
    }

    @Deprecated
    Calendar getFiredAt();

    default Instant getInstant() {
        return getFiredAt().toInstant();
    }

    void setEventId(String id);

    String getEventId();

    void setServiceMediator(IServiceMediator serviceMediator);

    IServiceMediator getServiceMediator();

}
