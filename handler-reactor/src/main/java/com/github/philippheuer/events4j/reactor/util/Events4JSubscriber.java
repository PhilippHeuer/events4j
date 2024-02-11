package com.github.philippheuer.events4j.reactor.util;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;

import java.util.function.Consumer;

public class Events4JSubscriber<E> implements Subscriber<E>, Disposable, IDisposable {

    private Subscription subscription;

    private final Consumer<E> consumer;

    private boolean isDisposed;

    public Events4JSubscriber(Consumer<E> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(E event) {
        consumer.accept(event);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void dispose() {
        if (!isDisposed) {
            subscription.cancel();
            isDisposed = true;
            subscription = null;
        }
    }

    @Override
    public boolean isDisposed() {
        return isDisposed;
    }
}
