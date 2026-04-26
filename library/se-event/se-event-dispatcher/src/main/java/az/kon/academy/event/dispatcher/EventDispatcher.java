package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;

import java.util.List;
import java.util.UUID;

public interface EventDispatcher {
    <T extends AbstractEvent> void dispatch(List<T> events, UUID correlationId, UUID causationId);

    default <T extends AbstractEvent> void dispatch(T event, UUID correlationId, UUID causationId) {
         dispatch(List.of(event), correlationId, causationId);
    }
}

