package az.example;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;

@Event(version = 1)
public class OrderCreatedEvent extends DomainEvent {
    private final String customerId;

    public OrderCreatedEvent(String orderId, String customerId, OffsetDateTime occurredAt) {
        super(orderId, occurredAt);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

}
