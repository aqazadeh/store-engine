package az.kon.academy.event.handler.exception;


import az.kon.academy.event.AbstractEvent;

public class DuplicateEventHandlerException extends EventHandlerException {
    public DuplicateEventHandlerException(String eventName) {
        super("Duplicate event handler detected for event: " + eventName);
    }
}
