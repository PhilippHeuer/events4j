package com.github.philippheuer.events4j.core.services;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.api.service.IServiceMediator;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * The ServiceMediator
 * <p>
 * The ServiceMediator provides access to 3rd party services for your custom events
 */
public final class ServiceMediator implements IServiceMediator {

    /**
     * Holds a reference of the EventManager
     */
    @Getter
    private final IEventManager eventManager;

    /**
     * Holds 3rd party service references
     */
    private final Map<String, Object> serviceReferences = new HashMap<>();

    /**
     * The Constructor.
     *
     * @param eventManager The EventManager
     */
    public ServiceMediator(final IEventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Add a service to the ServiceMediator.
     *
     * @param serviceName     The ServiceName
     * @param serviceInstance The ServiceInstance
     */
    public void addService(@NonNull String serviceName, @NonNull Object serviceInstance) {
        serviceReferences.put(serviceName, serviceInstance);
    }

    /**
     * Gets a service from the ServiceMediator
     *
     * @param serviceClass The ServiceClass you expect
     * @param serviceName  The ServiceName
     * @param <T>          The type of the Service
     * @return The ServiceInstance
     */
    @NonNull
    public <T> T getService(@NonNull Class<T> serviceClass, @NonNull String serviceName) {
        Object serviceInstance = serviceReferences.get(serviceName);

        if (serviceClass.isInstance(serviceInstance)) {
            //noinspection unchecked
            return (T) serviceReferences.get(serviceName);
        } else {
            throw new RuntimeException("Can't cast service " + serviceName + " to " + serviceClass.getSimpleName() + "!");
        }
    }
}
