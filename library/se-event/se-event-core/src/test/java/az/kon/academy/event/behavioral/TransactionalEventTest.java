package az.kon.academy.event.behavioral;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.annotation.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TransactionalEventTest {

    @Event(version = 1)
    static class PaymentProcessedEvent extends TransactionalEvent {
        protected PaymentProcessedEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }

        public PaymentProcessedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
            super(eventId, aggregateId, timestamp);
        }
    }

    private static final String AGGREGATE_ID = "payment-1";
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    @Nested
    @DisplayName("Type hierarchy")
    class TypeHierarchy {

        @Test
        @DisplayName("Is instance of AbstractEvent")
        void isInstanceOfAbstractEvent() {
            assertThat(new PaymentProcessedEvent(AGGREGATE_ID, NOW)).isInstanceOf(AbstractEvent.class);
        }

        @Test
        @DisplayName("Is instance of TransactionalEvent")
        void isInstanceOfTransactionalEvent() {
            assertThat(new PaymentProcessedEvent(AGGREGATE_ID, NOW)).isInstanceOf(TransactionalEvent.class);
        }

        @Test
        @DisplayName("Is NOT instance of DomainEvent")
        void isNotInstanceOfDomainEvent() {
            assertThat(new PaymentProcessedEvent(AGGREGATE_ID, NOW)).isNotInstanceOf(DomainEvent.class);
        }
    }

    @Nested
    @DisplayName("Constructor")
    class Construction {

        @Test
        @DisplayName("Protected constructor sets aggregateId and timestamp, generates eventId")
        void protectedConstructorSetsFields() {
            var event = new PaymentProcessedEvent(AGGREGATE_ID, NOW);
            assertThat(event.getAggregateId()).isEqualTo(AGGREGATE_ID);
            assertThat(event.getTimestamp()).isEqualTo(NOW);
            assertThat(event.getEventId()).isNotNull();
        }

        @Test
        @DisplayName("Public UUID constructor preserves eventId")
        void publicConstructorPreservesEventId() {
            var id = UUID.randomUUID();
            var event = new PaymentProcessedEvent(id, AGGREGATE_ID, NOW);
            assertThat(event.getEventId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("Dispatcher routing marker")
    class DispatcherMarker {

        @Test
        @DisplayName("instanceof check for TransactionalEvent returns true")
        void instanceofTransactionalEventReturnsTrue() {
            AbstractEvent event = new PaymentProcessedEvent(AGGREGATE_ID, NOW);
            assertThat(event instanceof TransactionalEvent).isTrue();
        }

        @Test
        @DisplayName("instanceof check for DomainEvent returns false")
        void instanceofDomainEventReturnsFalse() {
            AbstractEvent event = new PaymentProcessedEvent(AGGREGATE_ID, NOW);
            assertThat(event instanceof DomainEvent).isFalse();
        }
    }
}
