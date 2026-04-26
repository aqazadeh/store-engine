package az.kon.academy.event;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class EventMessage<T extends AbstractEvent> {
    private final UUID id;
    private final String aggregateId;
    private final OffsetDateTime timestamp;
    private final EventMessageHeader header;
    private final T payload;

    public EventMessage(T event, UUID correlationId, UUID causationId, String triggerBy) {
        this.id = event.getEventId();
        this.aggregateId = event.getAggregateId();
        this.timestamp = event.getTimestamp();
        this.header = EventMessageHeader.of(event.getClass().getName(), correlationId, causationId, triggerBy);
        this.payload = event;
    }

    public static <T extends AbstractEvent> EventMessage<T> of(T event, UUID correlationId, UUID causationId, String triggerBy) {
        return new EventMessage<T>(event, correlationId, causationId, triggerBy);
    }

    public static <T extends AbstractEvent> EventMessage<T> of(T event, UUID correlationId, String triggerBy) {
        return EventMessage.of(event, correlationId, null, triggerBy);
    }

    public static <T extends AbstractEvent> EventMessage<T> ofSystem(T event, UUID correlationId, UUID causationId) {
        return new EventMessage<T>(event, correlationId, causationId, "system");
    }

    public static <T extends AbstractEvent> EventMessage<T> ofSystem(T event, UUID correlationId) {
        return EventMessage.ofSystem(event, correlationId, null);
    }
}
