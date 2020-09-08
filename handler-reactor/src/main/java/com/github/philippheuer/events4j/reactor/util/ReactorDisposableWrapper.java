package com.github.philippheuer.events4j.reactor.util;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import lombok.Getter;
import reactor.core.Disposable;

public class ReactorDisposableWrapper implements IDisposable, Disposable {

    @Getter
    public Disposable disposable;

    public ReactorDisposableWrapper(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void dispose() {
        disposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return disposable.isDisposed();
    }

}
