package com.github.philippheuer.events4j.api.domain;

import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public interface IEventSubscription extends IDisposable {

    @NonNull
    String getId();

    @NonNull
    @SuppressWarnings("rawtypes")
    Class getEventType();

    @NonNull
    @SuppressWarnings("rawtypes")
    Consumer getConsumer();

}
