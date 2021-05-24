# *Events4J*

# Description

A simple wrapper to dispatch/consume events.

# Import

Maven:

Add the repository to your pom.xml with:
```xml
<repositories>
    <repository>
      <id>central</id>
      <url>https://repo1.maven.org/maven2/</url>
    </repository>
</repositories>
```
and the dependency: (latest, you should use the actual version here)

```xml
<dependency>
    <groupId>com.github.philippheuer.events4j</groupId>
    <artifactId>events4j</artifactId>
    <version>0.9.7</version>
    <type>pom</type>
</dependency>
```

Gradle:

Add the repository to your build.gradle with:
```groovy
repositories {
    mavenCentral()
}
```

and the dependency:
```groovy
compile 'com.github.philippheuer.events4j:events4j:0.9.7'
```

# Usage

## Initialization

```java
// new instance
EventManager eventManager = new EventManager();

// register modules automatically
eventManager.autoDiscovery();
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
    log.info("Received event [{}] that was fired at {}.",
        event.getEventId(),
        event.getFiredAt().toInstant().toString());
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
        System.out.println("TestEvent Listener Executed.");
    }

}
```

*Register the Consumer*
```java
eventManager.registerListener(new TestEventListener());
```

#### Spring Events

Include the Events4J-Spring Dependency and use spring properties to configure the handler:

```yaml
events4j.handler.spring.enabled: true
```

*The Consumer*

```java
@EventListener
public void handleContextStart(TestEvent testEvent) {
    System.out.println("Spring Event received.");
}
```

## License

Released under the [MIT License](./LICENSE).
