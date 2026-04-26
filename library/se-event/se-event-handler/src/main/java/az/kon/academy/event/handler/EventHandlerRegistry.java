package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;

public interface EventHandlerRegistry {
    void registerHandler(BaseEventHandler<? extends AbstractEvent> handler, Class<? extends AbstractEvent> eventType);
}
