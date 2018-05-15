# *Events4J*

# Description

A simple wrapper to dispatch/consume events using Reactor.

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
