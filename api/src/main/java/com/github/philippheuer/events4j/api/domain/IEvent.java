package com.github.philippheuer.events4j.api.domain;

import com.github.philippheuer.events4j.api.service.IServiceMediator;

import java.util.Calendar;

public interface IEvent {

    void setFiredAt(Calendar calendar);

    Calendar getFiredAt();

    void setEventId(String id);

    String getEventId();

    void setServiceMediator(IServiceMediator serviceMediator);

    IServiceMediator getServiceMediator();

}
