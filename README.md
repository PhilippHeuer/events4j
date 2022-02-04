# *Events4J*

[![Latest](https://img.shields.io/github/release/PhilippHeuer/events4j/all.svg?style=flate&label=latest)](https://search.maven.org/search?q=com.github.philippheuer.events4j)

Click the module name in the table below for specific import instructions. (gradle, maven, ...)

| Module                                                                                                                             | Description                                        | Coordinates                                                      |
|:-----------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------|------------------------------------------------------------------|
| [events4j-api](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-api/0.10.0/jar)                         | contains all interfaces                            | com.github.philippheuer.events4j:events4j-api:0.10.0             |
| [events4j-core](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-core/0.10.0/jar)                       | contains the EventManager implementation           | com.github.philippheuer.events4j:events4j-core:0.10.0            |
| [events4j-kotlin](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-kotlin/0.10.0/jar)                   | improvements for usage in kotlin projects          | com.github.philippheuer.events4j:events4j-kotlin:0.10.0          |
| [events4j-handler-simple](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-simple/0.10.0/jar)   | a simple synchronous event handler                 | com.github.philippheuer.events4j:events4j-handler-simple:0.10.0  |
| [events4j-handler-reactor](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-reactor/0.10.0/jar) | a event handler using project reactor              | com.github.philippheuer.events4j:events4j-handler-reactor:0.10.0 |
| [events4j-handler-spring](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-spring/0.10.0/jar)   | forward events to spring ApplicationEventPublisher | com.github.philippheuer.events4j:events4j-handler-spring:0.10.0  |

# Description

**Events4J** provides the following features:

- publish events
- register consumers / listeners for events
- comes with a few prebuilt handlers that you can use out of the box
- provide your custom implementation to process events however you want

# Usage

## Initialization

```java
EventManager eventManager = new EventManager(); // new instance
eventManager.autoDiscovery(); // register modules automatically
```

## Event Producer

```java
TestEvent testEvent = new TestEvent();
eventManager.publish(testEvent);
```

## Event Consumer

#### Subscriber-based

```java
IEventManager eventManager = new EventManager();
ReactorEventHandler reactorEventHandler = new ReactorEventHandler(eventManager);
eventManager.registerEventHandler(reactorEventHandler);
```

The Consumer
```java
reactorEventHandler.onEvent(TestEvent.class).subscribe(event -> {
    log.info("TestEvent received");
});
```

#### Simple

If you want to use annotation-based events, you need to enable this feature. Annotation-based event consumers are disabled by default.

```java
IEventManager eventManager = new EventManager();
SimpleEventHandler simpleEventHandler = new SimpleEventHandler();
eventManager.registerEventHandler(simpleEventHandler);
```

*The Consumer*
```java
public class TestEventListener {

    @EventSubscriber
    public void onTestEvent(TestEvent testEvent) {
        System.out.println("TestEvent received");
    }

}
```

*Register the Consumer*
```java
eventManager.registerListener(new TestEventListener());
```

#### Spring Events

Configure the following in your `application.properties` to enable spring application events:

```yaml
events4j.handler.spring.enabled: true
```

*The Consumer*

```java
@EventListener
public void handleContextStart(TestEvent testEvent) {
    System.out.println("TestEvent received");
}
```

## Kotlin

The kotlin module allows the usage of [flows](https://kotlinlang.org/docs/flow.html) to consume events.

```kotlin
eventManager.flowOn<TestEvent>()
    .collect { testEvent ->
        println("TestEvent received")
    }
```

## License

Released under the [MIT License](./LICENSE).
