package az.kon.academy.event.handler;


import az.kon.academy.event.AbstractEvent;

import java.util.List;

public interface DomainEventPublisher {

    default <E extends AbstractEvent> void publish(List<E> events) {
        events.forEach(this::publish);
    }

    <E extends AbstractEvent> void publish(E event);
}
