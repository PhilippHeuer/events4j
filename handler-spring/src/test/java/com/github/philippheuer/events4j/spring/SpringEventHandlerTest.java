package com.github.philippheuer.events4j.spring;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.spring.domain.TestEventObject;
import com.github.philippheuer.events4j.spring.domain.TestEventObjectExtended;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {SpringEventHandler.class})
@EnableAutoConfiguration
public class SpringEventHandlerTest {

    @Test
    public void testOnEventListeners(@Autowired SpringEventHandler eventHandler) throws Exception {
        AtomicInteger eventsHandled = new AtomicInteger(0);

        // Consumer based handler
        IDisposable disposable = eventHandler.onEvent(TestEventObject.class, testEvent -> {
            eventsHandled.addAndGet(1);
        });

        // Dispatch
        eventHandler.publish(new TestEventObject());
        eventHandler.publish(new TestEventObject());

        // Dispose Handler
        disposable.dispose();

        // Dispatch 2nd Event
        eventHandler.publish(new TestEventObject());

        // Verify
        Assertions.assertEquals(2, eventsHandled.get(), "one event should have been handled");
    }

    @Test
    public void testOnEventListenersInheritance(@Autowired SpringEventHandler eventHandler) throws Exception {
        AtomicInteger eventsHandled = new AtomicInteger(0);

        // Consumer based handler
        eventHandler.onEvent(TestEventObject.class, testEvent -> {
            eventsHandled.addAndGet(1);
        });
        eventHandler.onEvent(TestEventObjectExtended.class, testEvent -> {
            eventsHandled.addAndGet(1);
        });

        // Dispatch
        eventHandler.publish(new TestEventObjectExtended());

        // Verify
        Assertions.assertEquals(2, eventsHandled.get());
    }

}
