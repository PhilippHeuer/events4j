package com.github.philippheuer.events4j.exception;

public class EventBufferOverflowException extends IllegalStateException {

    /**
     * Constructs an EventBufferOverflowException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public EventBufferOverflowException() {
        super();
    }

    /**
     * Constructs an EventBufferOverflowException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public EventBufferOverflowException(String s) {
        super(s);
    }

}
