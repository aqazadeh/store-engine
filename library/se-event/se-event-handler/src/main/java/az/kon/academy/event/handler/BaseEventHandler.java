package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;

public interface BaseEventHandler<E extends AbstractEvent> {
    void handle(E event);
}
