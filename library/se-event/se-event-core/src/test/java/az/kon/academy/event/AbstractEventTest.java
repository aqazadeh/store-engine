package az.kon.academy.event;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AbstractEventTest {

    @Event(version = 1)
    static class SampleEvent extends DomainEvent {
        protected SampleEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }

        public SampleEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
            super(eventId, aggregateId, timestamp);
        }
    }

    @Event(version = 5)
    static class VersionedEvent extends DomainEvent {
        protected VersionedEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }
    }

    static class UnannotatedEvent extends DomainEvent {
        protected UnannotatedEvent(String aggregateId, OffsetDateTime timestamp) {
            super(aggregateId, timestamp);
        }
    }

    private static final String AGGREGATE_ID = "order-123";
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    @Nested
    @DisplayName("Constructor validation")
    class ConstructorValidation {

        @Test
        @DisplayName("Protected constructor auto-generates non-null UUID")
        void protectedConstructorGeneratesEventId() {
            var event = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event.getEventId()).isNotNull();
        }

        @Test
        @DisplayName("Two events created separately have different UUIDs")
        void twoEventsHaveDifferentEventIds() {
            var event1 = new SampleEvent(AGGREGATE_ID, NOW);
            var event2 = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
        }

        @Test
        @DisplayName("Public constructor preserves provided UUID")
        void publicConstructorPreservesUUID() {
            var id = UUID.randomUUID();
            var event = new SampleEvent(id, AGGREGATE_ID, NOW);
            assertThat(event.getEventId()).isEqualTo(id);
        }

        @Test
        @DisplayName("All fields are set correctly via public constructor")
        void publicConstructorSetsAllFields() {
            var id = UUID.randomUUID();
            var event = new SampleEvent(id, AGGREGATE_ID, NOW);
            assertThat(event.getAggregateId()).isEqualTo(AGGREGATE_ID);
            assertThat(event.getTimestamp()).isEqualTo(NOW);
        }

        @Test
        @DisplayName("Null eventId throws NullPointerException")
        void nullEventIdThrowsNPE() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new SampleEvent(null, AGGREGATE_ID, NOW))
                    .withMessageContaining("EventId must not be null");
        }

        @Test
        @DisplayName("Null aggregateId throws NullPointerException")
        void nullAggregateIdThrowsNPE() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new SampleEvent(UUID.randomUUID(), null, NOW))
                    .withMessageContaining("aggregateId must not be null");
        }

        @Test
        @DisplayName("Null timestamp throws NullPointerException")
        void nullTimestampThrowsNPE() {
            assertThatNullPointerException()
                    .isThrownBy(() -> new SampleEvent(UUID.randomUUID(), AGGREGATE_ID, null))
                    .withMessageContaining("timestamp must not be null");
        }
    }

    @Nested
    @DisplayName("@Event annotation enforcement")
    class AnnotationEnforcement {

        @Test
        @DisplayName("Missing @Event annotation throws IllegalStateException")
        void missingAnnotationThrowsIllegalState() {
            assertThatIllegalStateException()
                    .isThrownBy(() -> new UnannotatedEvent(AGGREGATE_ID, NOW))
                    .withMessageContaining("UnannotatedEvent")
                    .withMessageContaining("@Event");
        }

        @Test
        @DisplayName("Version is read from @Event annotation")
        void versionReadFromAnnotation() {
            var event = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event.getVersion()).isEqualTo(1);
        }

        @Test
        @DisplayName("Higher version number is read correctly")
        void higherVersionReadCorrectly() {
            var event = new VersionedEvent(AGGREGATE_ID, NOW);
            assertThat(event.getVersion()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Equality — based only on eventId")
    class Equality {

        @Test
        @DisplayName("Same eventId with different fields are equal")
        void sameEventIdAreEqual() {
            var id = UUID.randomUUID();
            var event1 = new SampleEvent(id, AGGREGATE_ID, NOW);
            var event2 = new SampleEvent(id, "different-aggregate", NOW.plusDays(1));
            assertThat(event1).isEqualTo(event2);
        }

        @Test
        @DisplayName("Same eventId produces same hashCode")
        void sameEventIdSameHashCode() {
            var id = UUID.randomUUID();
            var event1 = new SampleEvent(id, AGGREGATE_ID, NOW);
            var event2 = new SampleEvent(id, AGGREGATE_ID, NOW);
            assertThat(event1.hashCode()).isEqualTo(event2.hashCode());
        }

        @Test
        @DisplayName("Different eventIds are not equal")
        void differentEventIdsNotEqual() {
            var event1 = new SampleEvent(AGGREGATE_ID, NOW);
            var event2 = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event1).isNotEqualTo(event2);
        }

        @Test
        @DisplayName("Event is not equal to null")
        void notEqualToNull() {
            var event = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Event is equal to itself")
        void equalToItself() {
            var event = new SampleEvent(AGGREGATE_ID, NOW);
            assertThat(event).isEqualTo(event);
        }
    }
}
