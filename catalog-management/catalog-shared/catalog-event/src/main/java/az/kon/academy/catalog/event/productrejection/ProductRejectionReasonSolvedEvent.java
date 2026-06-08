package az.kon.academy.catalog.event.productrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductRejectionReasonSolvedEvent extends DomainEvent implements ProductRejectionReasonEvent {

    private final Boolean solved;

    public ProductRejectionReasonSolvedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                              Integer version, Boolean solved) {
        super(eventId, aggregateId, timestamp, version);
        this.solved = solved;
    }

    private ProductRejectionReasonSolvedEvent(String aggregateId, OffsetDateTime timestamp, Boolean solved) {
        super(aggregateId, timestamp);
        this.solved = solved;
    }

    public static ProductRejectionReasonSolvedEvent of(String aggregateId, OffsetDateTime timestamp, Boolean solved) {
        return new ProductRejectionReasonSolvedEvent(aggregateId, timestamp, solved);
    }
}
