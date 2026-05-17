package az.kon.academy.event;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public final class EventMessageHeader {
    private final String eventType;
    private final UUID correlationId; // action_id
    private final UUID causationId; // previous_action_id
    private final String triggerBy;
    private final Integer version;

    public EventMessageHeader(String eventType, UUID correlationId, UUID causationId, String triggerBy, Integer version) {
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.triggerBy = triggerBy;
        this.version = version;
    }

    public static EventMessageHeader of(String eventType, UUID correlationId, UUID causationId, String triggerBy, Integer version) {
        return new EventMessageHeader(eventType, correlationId, causationId, triggerBy, version);
    }
}
