package az.kon.academy.event.behavioral;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.annotation.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DomainEventTest {

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        protected OrderCreatedEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }

        public OrderCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
            super(eventId, aggregateId, timestamp);
        }
    }

    private static final String AGGREGATE_ID = "order-1";
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    @Nested
    @DisplayName("Type hierarchy")
    class TypeHierarchy {

        @Test
        @DisplayName("Is instance of AbstractEvent")
        void isInstanceOfAbstractEvent() {
            assertThat(new OrderCreatedEvent(AGGREGATE_ID, NOW)).isInstanceOf(AbstractEvent.class);
        }

        @Test
        @DisplayName("Is instance of DomainEvent")
        void isInstanceOfDomainEvent() {
            assertThat(new OrderCreatedEvent(AGGREGATE_ID, NOW)).isInstanceOf(DomainEvent.class);
        }

        @Test
        @DisplayName("Is NOT instance of TransactionalEvent")
        void isNotInstanceOfTransactionalEvent() {
            assertThat(new OrderCreatedEvent(AGGREGATE_ID, NOW)).isNotInstanceOf(TransactionalEvent.class);
        }
    }

    @Nested
    @DisplayName("Constructor")
    class Construction {

        @Test
        @DisplayName("Protected constructor sets aggregateId and timestamp, generates eventId")
        void protectedConstructorSetsFields() {
            var event = new OrderCreatedEvent(AGGREGATE_ID, NOW);
            assertThat(event.getAggregateId()).isEqualTo(AGGREGATE_ID);
            assertThat(event.getTimestamp()).isEqualTo(NOW);
            assertThat(event.getEventId()).isNotNull();
        }

        @Test
        @DisplayName("Public UUID constructor preserves eventId")
        void publicConstructorPreservesEventId() {
            var id = UUID.randomUUID();
            var event = new OrderCreatedEvent(id, AGGREGATE_ID, NOW);
            assertThat(event.getEventId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("Dispatcher routing marker")
    class DispatcherMarker {

        @Test
        @DisplayName("instanceof check for DomainEvent returns true")
        void instanceofDomainEventReturnsTrue() {
            AbstractEvent event = new OrderCreatedEvent(AGGREGATE_ID, NOW);
            assertThat(event instanceof DomainEvent).isTrue();
        }

        @Test
        @DisplayName("instanceof check for TransactionalEvent returns false")
        void instanceofTransactionalEventReturnsFalse() {
            AbstractEvent event = new OrderCreatedEvent(AGGREGATE_ID, NOW);
            assertThat(event instanceof TransactionalEvent).isFalse();
        }
    }
}
