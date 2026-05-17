package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;

import java.util.Optional;

public interface EventHandlerRegistry {
    Optional<BaseEventHandler> getHandlerForEvent(Class<? extends AbstractEvent> eventType);

    void registerHandler(BaseEventHandler<? extends AbstractEvent> handler, Class<? extends AbstractEvent> eventType);
}
