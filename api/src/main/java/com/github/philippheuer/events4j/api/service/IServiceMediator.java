package com.github.philippheuer.events4j.api.service;

public interface IServiceMediator {

    /**
     * Add a service to the ServiceMediator.
     *
     * @param serviceName     The ServiceName
     * @param serviceInstance The ServiceInstance
     */
    void addService(String serviceName, Object serviceInstance);

    /**
     * Gets a service from the ServiceMediator
     *
     * @param serviceClass The ServiceClass you expect
     * @param serviceName  The ServiceName
     * @param <T>          The type of the Service
     * @return The ServiceInstance
     */
    <T extends Object> T getService(Class<T> serviceClass, String serviceName);

}
