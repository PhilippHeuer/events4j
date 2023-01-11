package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.IEventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.InvocationTargetException;

/**
 * Events4J Spring AutoConfiguration
 */
@AutoConfiguration
@ComponentScan("com.github.philippheuer.events4j.spring")
@ConditionalOnProperty(name = "events4j.handler.spring.enabled", havingValue = "true", matchIfMissing = true)
public class Events4JSpringAutoConfiguration {

    private final SpringEventHandler springEventHandler;

    private final String eventManagerClass;

    @Autowired
    public Events4JSpringAutoConfiguration(SpringEventHandler springEventHandler, @Value("${events4j.handler.spring.eventManagerClass:com.github.philippheuer.events4j.core.EventManager}") String eventManagerClass) {
        this.springEventHandler = springEventHandler;
        this.eventManagerClass = eventManagerClass;
    }

    /**
     * @return a IEventManager with a registered spring event handler
     */
    @Bean
    public IEventManager getEventManager() {
        IEventManager eventManager = null;

        try {
            Class<? extends IEventManager> emClass = (Class<? extends IEventManager>) Class.forName(eventManagerClass);
            eventManager = emClass.getDeclaredConstructor(new Class[0]).newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            throw new RuntimeException("failed to initialize EventManager, add the events4j core module or set a custom EventManager class in events4j.handler.spring.eventManagerClass.", ex);
        }

        eventManager.registerEventHandler(springEventHandler);
        return eventManager;
    }
}
