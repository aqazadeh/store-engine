package az.kon.academy.event;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public abstract class AbstractEvent {
    private final UUID eventId;
    private final String aggregateId;
    private final OffsetDateTime timestamp;

    protected AbstractEvent(String aggregateId, OffsetDateTime timestamp) {// maybe kafka connection lost when writing is completed dont panic next worker checks is written or not
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;
    }

    public AbstractEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;

    }
}
