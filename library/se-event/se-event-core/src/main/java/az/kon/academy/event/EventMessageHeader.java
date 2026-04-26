package az.kon.academy.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class EventMessageHeader {
    private final String eventType;
    private final UUID correlationId; // action_id
    private final UUID causationId; // previous_action_id
    private final String triggerBy;

    public EventMessageHeader(String eventType, UUID correlationId, UUID causationId, String triggerBy) {
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.triggerBy = triggerBy;
    }

    public static EventMessageHeader of(String eventType, UUID correlationId, UUID causationId, String triggerBy) {
        return new EventMessageHeader(eventType, correlationId, causationId, triggerBy);
    }
}
