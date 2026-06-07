package az.kon.academy.catalog.event.brandrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandRejectionReasonSolvedEvent extends DomainEvent implements BrandRejectionReasonEvent {

    private final Boolean solved;

    public BrandRejectionReasonSolvedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                           Integer version, Boolean solved) {
        super(eventId, aggregateId, timestamp, version);
        this.solved = solved;
    }

    private BrandRejectionReasonSolvedEvent(String aggregateId, OffsetDateTime timestamp, Boolean solved) {
        super(aggregateId, timestamp);
        this.solved = solved;
    }

    public static BrandRejectionReasonSolvedEvent of(String aggregateId, OffsetDateTime timestamp, Boolean solved) {
        return new BrandRejectionReasonSolvedEvent(aggregateId, timestamp, solved);
    }
}