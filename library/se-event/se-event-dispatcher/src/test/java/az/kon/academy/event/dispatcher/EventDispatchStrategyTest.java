package az.kon.academy.event.dispatcher;

import az.kon.academy.event.EventMessage;
import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class EventDispatchStrategyTest {

    // --- Fixtures ---

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        OrderCreatedEvent() { super("order-1", OffsetDateTime.now()); }
    }

    // --- Tests ---

    @Nested
    @DisplayName("NOOP implementation")
    class Noop {

        private final EventDispatchStrategy noop = EventDispatchStrategy.NOOP;

        @Test
        @DisplayName("NOOP is an instance of NoopEventDispatchStrategy")
        void noopIsCorrectType() {
            assertThat(noop).isInstanceOf(EventDispatchStrategy.NoopEventDispatchStrategy.class);
        }

        @Test
        @DisplayName("NOOP constant is always the same instance")
        void noopIsSingleton() {
            assertThat(EventDispatchStrategy.NOOP).isSameAs(EventDispatchStrategy.NOOP);
        }

        @Test
        @DisplayName("proceed() with a non-empty list does not throw")
        void proceedWithNonEmptyListDoesNotThrow() {
            var event = new OrderCreatedEvent();
            var message = EventMessage.of(event, UUID.randomUUID(), UUID.randomUUID(), "user");

            assertThatNoException()
                    .isThrownBy(() -> noop.proceed(List.of(message)));
        }

        @Test
        @DisplayName("proceed() with an empty list does not throw")
        void proceedWithEmptyListDoesNotThrow() {
            assertThatNoException()
                    .isThrownBy(() -> noop.proceed(List.of()));
        }
    }
}
