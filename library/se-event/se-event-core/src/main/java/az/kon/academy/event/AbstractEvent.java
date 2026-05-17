package az.kon.academy.event;

import az.kon.academy.event.annotation.Event;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Getter
@EqualsAndHashCode(of = "eventId")
public abstract class AbstractEvent {
    private final UUID eventId;
    private final String aggregateId;
    private final OffsetDateTime timestamp;
    private final Integer version;

    public AbstractEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
        Objects.requireNonNull(eventId, "EventId must not be null");
        Objects.requireNonNull(aggregateId, "aggregateId must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");

        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;
        this.version = this.extractVersion();
    }

    protected AbstractEvent(String aggregateId, OffsetDateTime timestamp) {
        this(UUID.randomUUID(), aggregateId, timestamp);
    }

    private Integer extractVersion() {
        Optional<Event> annotation = Optional.ofNullable(this.getClass().getAnnotation(Event.class));
        if(annotation.isEmpty()) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " must be annotated with @Event");
        }
        return annotation.get().version();
    }
}
