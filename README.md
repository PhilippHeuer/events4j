# *Events4J*

# Description

A simple wrapper to dispatch/consume events using Reactor.

# Import

Maven:

Add the repository to your pom.xml with:
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
and the dependency: (latest, you should use the actual version here)

```xml
<dependency>
    <groupId>com.github.philippheuer.events4j</groupId>
    <artifactId>events4j</artifactId>
    <version>0.4.0</version>
    <type>pom</type>
</dependency>
```

Gradle:

Add the repository to your build.gradle with:
```groovy
repositories {
	jcenter()
}
```

and the dependency:
```groovy
compile 'com.github.philippheuer.events4j:events4j:0.4.0'
```

# Usage

## Initialization

```java
EventManager eventManager = new EventManager();
```

## Event Producer

```java
TestEvent testEvent = new TestEvent();
eventManager.dispatchEvent(testEvent);
```

## Event Consumer

#### Subscriber-based

```java
eventManager.onEvent(TestEvent.class).subscribe(event -> {
    log.info("Received event [{}] that was fired at {}.",
        event.getEventId(),
        event.getFiredAt().toInstant().toString());
});
```

#### Annotation-based

If you want to use annotation-based events, you need to enable this feature. Annotation-based event consumers are disabled by default.

```java
eventManager.enableAnnotationBasedEvents();
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

## License

Released under the [MIT License](./LICENSE).
