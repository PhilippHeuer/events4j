# Events4J

## Description



## Usage

#### Initialization

```java
EventManager eventManager = new EventManager();
```

#### Event Producer

```java
TestEvent testEvent = new TestEvent();
eventManager.dispatchEvent(testEvent);
```

#### Event Consumer

```java
eventManager.onEvent(TestEvent.class).subscribe(event -> {
    log.info("Received event [{}] that was fired at {}.",
        event.getEventId(),
        event.getFiredAt().toInstant().toString());
});
```

## License

Released under the [MIT License](./LICENSE).
