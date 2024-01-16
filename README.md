# *Events4J*

[![Latest](https://img.shields.io/github/release/PhilippHeuer/events4j/all.svg?style=flate&label=latest)](https://search.maven.org/search?q=com.github.philippheuer.events4j)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=github-com-133151090&metric=bugs)](https://sonarcloud.io/summary/overall?id=github-com-133151090)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=github-com-133151090&metric=code_smells)](https://sonarcloud.io/summary/overall?id=github-com-133151090)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=github-com-133151090&metric=duplicated_lines_density)](https://sonarcloud.io/summary/overall?id=github-com-133151090)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=github-com-133151090&metric=coverage)](https://sonarcloud.io/component_measures?metric=Coverage&view=list&id=github-com-133151090)

Click the module name in the table below for specific import instructions. (gradle, maven, ...)

| Module                                                                                                                                                                                                                                 | Javadoc                                                                                                                                                                                                       | Description                                        |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------|
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-api?label=events4j-api)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-api)                                     | [![Javadoc](https://javadoc.io/badge2/com.github.philippheuer.events4j/events4j-api/javadoc.svg?label=javadoc)](https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-api)                         | contains all interfaces                            |
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-core?label=events4j-core)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-core)                                  | [![Javadoc](https://javadoc.io/badge2/com.github.philippheuer.events4j/events4j-core/javadoc.svg?label=javadoc)](https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-core)                       | contains the EventManager implementation           |
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-kotlin?label=events4j-kotlin)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-kotlin)                            |                                                                                                                                                                                                               | improvements for usage in kotlin projects          |
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-handler-simple?label=events4j-handler-simple)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-simple)    | [![Javadoc](https://javadoc.io/badge2/com.github.philippheuer.events4j/events4j-handler-simple/javadoc.svg?label=javadoc)](https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-simple)   | a simple synchronous event handler                 |
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-handler-reactor?label=events4j-handler-reactor)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-reactor) | [![Javadoc](https://javadoc.io/badge2/com.github.philippheuer.events4j/events4j-handler-reactor/javadoc.svg?label=javadoc)](https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-reactor) | a event dispatcher using project reactor           |
| [![Lib](https://img.shields.io/maven-central/v/com.github.philippheuer.events4j/events4j-handler-spring?label=events4j-handler-spring)](https://search.maven.org/artifact/com.github.philippheuer.events4j/events4j-handler-spring)    | [![Javadoc](https://javadoc.io/badge2/com.github.philippheuer.events4j/events4j-handler-spring/javadoc.svg?label=javadoc)](https://javadoc.io/doc/com.github.philippheuer.events4j/events4j-handler-spring)   | forward events to spring ApplicationEventPublisher |

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
