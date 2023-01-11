package com.github.philippheuer.events4j.spring.domain;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;

@Getter
public class SpringListenerSubscription implements IDisposable {

    @Getter
    private boolean isDisposed = false;

    @Getter(AccessLevel.NONE)
    private final ApplicationEventMulticaster applicationEventMulticaster;

    @Getter(AccessLevel.NONE)
    private final ApplicationListener listener;

    public SpringListenerSubscription(ApplicationEventMulticaster applicationEventMulticaster, ApplicationListener listener) {
        this.applicationEventMulticaster = applicationEventMulticaster;
        this.listener = listener;
    }

    /**
     * Dispose
     */
    public void dispose() {
        if (!isDisposed) {
            applicationEventMulticaster.removeApplicationListener(listener);

            // disposed
            isDisposed = true;
        }
    }

    public static SpringListenerSubscription of(ApplicationEventMulticaster applicationEventMulticaster, ApplicationListener listener) {
        return new SpringListenerSubscription(applicationEventMulticaster, listener);
    }

}
