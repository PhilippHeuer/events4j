package com.github.philippheuer.events4j.services;

import com.github.philippheuer.events4j.EventManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The ServiceMediator
 * <p>
 * The ServiceMediator provides access to 3rd party services for your custom events
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
public final class ServiceMediator {

    /**
     * Holds a reference of the EventManager
     */
    private final EventManager eventManager;

    /**
     * Holds 3rd party service references
     */
    private final Map<String, Object> serviceReferences = new HashMap<>();

    /**
     * Constructor
     *
     * @param eventManager The EventManager
     */
    public ServiceMediator(final EventManager eventManager) {
        this.eventManager = eventManager;
    }
}
