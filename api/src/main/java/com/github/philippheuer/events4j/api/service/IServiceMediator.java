package com.github.philippheuer.events4j.api.service;

import org.jspecify.annotations.NonNull;

public interface IServiceMediator {

    /**
     * Add a service to the ServiceMediator.
     *
     * @param serviceName     The ServiceName
     * @param serviceInstance The ServiceInstance
     */
    void addService(@NonNull String serviceName, @NonNull Object serviceInstance);

    /**
     * Gets a service from the ServiceMediator
     *
     * @param serviceClass The ServiceClass you expect
     * @param serviceName  The ServiceName
     * @param <T>          The type of the Service
     * @throws RuntimeException if no service of the provided type and name is registered
     * @return The ServiceInstance
     */
    @NonNull
    <T> T getService(@NonNull Class<T> serviceClass, @NonNull String serviceName);

}
