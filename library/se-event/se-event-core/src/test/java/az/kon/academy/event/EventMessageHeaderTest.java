package az.kon.academy.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class EventMessageHeaderTest {

    private static final String EVENT_TYPE = "az.kon.academy.event.OrderCreated";
    private static final UUID CORRELATION_ID = UUID.randomUUID();
    private static final UUID CAUSATION_ID = UUID.randomUUID();
    private static final String TRIGGER_BY = "order-service";
    private static final Integer VERSION = 3;

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("Constructor sets all fields correctly")
        void constructorSetsAllFields() {
            var header = new EventMessageHeader(EVENT_TYPE, CORRELATION_ID, CAUSATION_ID, TRIGGER_BY, VERSION);
            assertThat(header.getEventType()).isEqualTo(EVENT_TYPE);
            assertThat(header.getCorrelationId()).isEqualTo(CORRELATION_ID);
            assertThat(header.getCausationId()).isEqualTo(CAUSATION_ID);
            assertThat(header.getTriggerBy()).isEqualTo(TRIGGER_BY);
            assertThat(header.getVersion()).isEqualTo(VERSION);
        }

        @Test
        @DisplayName("of() factory produces same result as constructor")
        void ofFactoryEquivalentToConstructor() {
            var fromConstructor = new EventMessageHeader(EVENT_TYPE, CORRELATION_ID, CAUSATION_ID, TRIGGER_BY, VERSION);
            var fromFactory = EventMessageHeader.of(EVENT_TYPE, CORRELATION_ID, CAUSATION_ID, TRIGGER_BY, VERSION);

            assertThat(fromFactory.getEventType()).isEqualTo(fromConstructor.getEventType());
            assertThat(fromFactory.getCorrelationId()).isEqualTo(fromConstructor.getCorrelationId());
            assertThat(fromFactory.getCausationId()).isEqualTo(fromConstructor.getCausationId());
            assertThat(fromFactory.getTriggerBy()).isEqualTo(fromConstructor.getTriggerBy());
            assertThat(fromFactory.getVersion()).isEqualTo(fromConstructor.getVersion());
        }

        @Test
        @DisplayName("causationId can be null (no previous action)")
        void causationIdCanBeNull() {
            var header = EventMessageHeader.of(EVENT_TYPE, CORRELATION_ID, null, TRIGGER_BY, VERSION);
            assertThat(header.getCausationId()).isNull();
        }
    }

    @Nested
    @DisplayName("toString()")
    class ToString {

        @Test
        @DisplayName("toString() does not throw")
        void toStringDoesNotThrow() {
            var header = EventMessageHeader.of(EVENT_TYPE, CORRELATION_ID, CAUSATION_ID, TRIGGER_BY, VERSION);
            assertThatNoException().isThrownBy(header::toString);
        }

        @Test
        @DisplayName("toString() contains eventType")
        void toStringContainsEventType() {
            var header = EventMessageHeader.of(EVENT_TYPE, CORRELATION_ID, CAUSATION_ID, TRIGGER_BY, VERSION);
            assertThat(header.toString()).contains(EVENT_TYPE);
        }

        @Test
        @DisplayName("toString() with null causationId does not throw")
        void toStringWithNullCausationIdDoesNotThrow() {
            var header = EventMessageHeader.of(EVENT_TYPE, CORRELATION_ID, null, TRIGGER_BY, VERSION);
            assertThatNoException().isThrownBy(header::toString);
        }
    }
}
