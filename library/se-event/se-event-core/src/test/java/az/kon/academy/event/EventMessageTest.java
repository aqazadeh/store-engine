package az.kon.academy.event;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class EventMessageTest {

    @Event(version = 2)
    static class SampleEvent extends DomainEvent {
        protected SampleEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }

        public SampleEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
            super(eventId, aggregateId, timestamp);
        }
    }

    private SampleEvent event;
    private UUID correlationId;
    private UUID causationId;

    @BeforeEach
    void setUp() {
        event = new SampleEvent("order-456", OffsetDateTime.now());
        correlationId = UUID.randomUUID();
        causationId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Field mapping from event to message")
    class FieldMapping {

        @Test
        @DisplayName("Message id equals event.eventId")
        void messageIdEqualsEventId() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getId()).isEqualTo(event.getEventId());
        }

        @Test
        @DisplayName("Message aggregateId equals event.aggregateId")
        void messageAggregateIdEqualsEventAggregateId() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getAggregateId()).isEqualTo(event.getAggregateId());
        }

        @Test
        @DisplayName("Message timestamp equals event.timestamp")
        void messageTimestampEqualsEventTimestamp() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getTimestamp()).isEqualTo(event.getTimestamp());
        }

        @Test
        @DisplayName("Message payload is the same event instance")
        void payloadIsSameEventInstance() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getPayload()).isSameAs(event);
        }

        @Test
        @DisplayName("Header version comes from event @Event annotation")
        void headerVersionComesFromEventAnnotation() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getHeader().getVersion()).isEqualTo(event.getVersion()).isEqualTo(2);
        }

        @Test
        @DisplayName("Header eventType is fully qualified class name")
        void headerEventTypeIsClassName() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getHeader().getEventType()).isEqualTo(event.getClass().getName());
        }
    }

    @Nested
    @DisplayName("Factory method: of()")
    class OfFactory {

        @Test
        @DisplayName("of() with all params sets correlationId and causationId")
        void ofWithAllParams() {
            var message = EventMessage.of(event, correlationId, causationId, "service");
            assertThat(message.getHeader().getCorrelationId()).isEqualTo(correlationId);
            assertThat(message.getHeader().getCausationId()).isEqualTo(causationId);
            assertThat(message.getHeader().getTriggerBy()).isEqualTo("service");
        }

        @Test
        @DisplayName("of() without causationId sets causationId to null")
        void ofWithoutCausationIdSetsNull() {
            var message = EventMessage.of(event, correlationId, "service");
            assertThat(message.getHeader().getCausationId()).isNull();
            assertThat(message.getHeader().getCorrelationId()).isEqualTo(correlationId);
        }
    }

    @Nested
    @DisplayName("Factory method: ofSystem()")
    class OfSystemFactory {

        @Test
        @DisplayName("ofSystem() with causationId sets triggerBy to 'system'")
        void ofSystemSetsTriggerByToSystem() {
            var message = EventMessage.ofSystem(event, correlationId, causationId);
            assertThat(message.getHeader().getTriggerBy()).isEqualTo("system");
            assertThat(message.getHeader().getCausationId()).isEqualTo(causationId);
        }

        @Test
        @DisplayName("ofSystem() without causationId sets causationId to null and triggerBy to 'system'")
        void ofSystemWithoutCausationId() {
            var message = EventMessage.ofSystem(event, correlationId);
            assertThat(message.getHeader().getTriggerBy()).isEqualTo("system");
            assertThat(message.getHeader().getCausationId()).isNull();
        }

        @Test
        @DisplayName("ofSystem() sets correlationId correctly")
        void ofSystemSetsCorrelationId() {
            var message = EventMessage.ofSystem(event, correlationId);
            assertThat(message.getHeader().getCorrelationId()).isEqualTo(correlationId);
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToString {

        @Test
        @DisplayName("toString() does not throw")
        void toStringDoesNotThrow() {
            var message = EventMessage.of(event, correlationId, "service");
            assertThatNoException().isThrownBy(message::toString);
        }

        @Test
        @DisplayName("toString() contains aggregateId")
        void toStringContainsAggregateId() {
            var message = EventMessage.of(event, correlationId, "service");
            assertThat(message.toString()).contains(event.getAggregateId());
        }
    }
}
