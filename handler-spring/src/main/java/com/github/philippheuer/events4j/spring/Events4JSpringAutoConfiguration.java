package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.IEventManager;
import com.github.philippheuer.events4j.core.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Events4J Spring AutoConfiguration
 */
@Configuration
@ComponentScan("com.github.philippheuer.events4j.spring")
@ConditionalOnProperty(name = "events4j.handler.spring.enabled", havingValue = "true", matchIfMissing = true)
public class Events4JSpringAutoConfiguration {

    @Autowired
    SpringEventHandler springEventHandler;

    @Bean
    public IEventManager getEventManager() {
        IEventManager eventManager = new EventManager();
        eventManager.registerEventHandler(springEventHandler);
        return eventManager;
    }

}
