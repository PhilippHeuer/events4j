package com.github.philippheuer.events4j.api.domain;

import java.util.function.Consumer;

public interface IEventSubscription extends IDisposable {

    String getId();

    @SuppressWarnings("rawtypes")
    Class getEventType();

    @SuppressWarnings("rawtypes")
    Consumer getConsumer();

}
