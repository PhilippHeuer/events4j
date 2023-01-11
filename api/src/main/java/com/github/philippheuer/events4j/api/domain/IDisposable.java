package com.github.philippheuer.events4j.api.domain;

public interface IDisposable {

    /**
     * This method is called to dispose of the resources held by the object that implements this interface.
     * The exact behavior of the method will depend on the implementation of the object, but it is typically used to release any resources that the object has acquired, such as open files or network connections.
     */
    void dispose();

    /**
     * This method is used to check if the object has been disposed of. It is typically used to check if it is safe to call the `dispose()` method again.
     * @return boolean
     */
    boolean isDisposed();

}
