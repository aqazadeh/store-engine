package az.kon.academy.event.handler.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventHandlerExceptionTest {

    @Nested
    @DisplayName("EventHandlerException")
    class EventHandlerExceptionTests {

        @Test
        @DisplayName("Extends RuntimeException")
        void extendsRuntimeException() {
            assertThat(new EventHandlerException("msg"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Stores the message correctly")
        void storesMessage() {
            assertThat(new EventHandlerException("test message").getMessage())
                    .isEqualTo("test message");
        }
    }

    @Nested
    @DisplayName("DuplicateEventHandlerException")
    class DuplicateEventHandlerExceptionTests {

        @Test
        @DisplayName("Extends EventHandlerException")
        void extendsEventHandlerException() {
            assertThat(new DuplicateEventHandlerException("SomeEvent"))
                    .isInstanceOf(EventHandlerException.class);
        }

        @Test
        @DisplayName("Message contains the given event name")
        void messageContainsEventName() {
            var ex = new DuplicateEventHandlerException("OrderCreatedEvent");
            assertThat(ex.getMessage()).contains("OrderCreatedEvent");
        }

        @Test
        @DisplayName("Message contains the 'Duplicate event handler detected' prefix")
        void messageContainsDuplicatePrefix() {
            var ex = new DuplicateEventHandlerException("SomeEvent");
            assertThat(ex.getMessage()).contains("Duplicate event handler detected");
        }
    }

    @Nested
    @DisplayName("EventHandlerNotFoundException")
    class EventHandlerNotFoundExceptionTests {

        @Test
        @DisplayName("Extends EventHandlerException")
        void extendsEventHandlerException() {
            assertThat(new EventHandlerNotFoundException("No handler for X"))
                    .isInstanceOf(EventHandlerException.class);
        }

        @Test
        @DisplayName("Stores the provided message as-is")
        void storesMessageAsProvided() {
            var msg = "No handler found for event type: OrderCreatedEvent";
            assertThat(new EventHandlerNotFoundException(msg).getMessage()).isEqualTo(msg);
        }
    }
}
