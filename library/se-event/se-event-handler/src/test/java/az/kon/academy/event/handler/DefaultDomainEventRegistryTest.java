package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import az.kon.academy.event.handler.exception.DuplicateEventHandlerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

class DefaultDomainEventRegistryTest {

    // --- Fixtures ---

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        OrderCreatedEvent() {
            super("order-1", OffsetDateTime.now());
        }
    }

    @Event(version = 1)
    static class OrderShippedEvent extends OrderCreatedEvent {
        // Subclass of OrderCreatedEvent — used for hierarchy lookup tests
    }

    @Event(version = 1)
    static class PaymentCompletedEvent extends DomainEvent {
        PaymentCompletedEvent() {
            super("payment-1", OffsetDateTime.now());
        }
    }

    static class TrackingHandler<E extends AbstractEvent> implements BaseEventHandler<E> {
        private final List<E> handled = new ArrayList<>();

        @Override
        public void handle(E event) {
            handled.add(event);
        }

        List<E> handled() {
            return handled;
        }
    }

    // --- Setup ---

    private DefaultDomainEventRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultDomainEventRegistry();
    }

    // --- Tests ---

    @Nested
    @DisplayName("registerHandler")
    class RegisterHandler {

        @Test
        @DisplayName("Registers handler successfully and makes it retrievable")
        void successfulRegistration() {
            var handler = new TrackingHandler<OrderCreatedEvent>();

            registry.registerHandler(handler, OrderCreatedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(handler);
        }

        @Test
        @DisplayName("Null handler throws NullPointerException with descriptive message")
        void nullHandlerThrowsNPE() {
            assertThatNullPointerException()
                    .isThrownBy(() -> registry.registerHandler(null, OrderCreatedEvent.class))
                    .withMessageContaining("handler must not be null");
        }

        @Test
        @DisplayName("Null eventType throws NullPointerException with descriptive message")
        void nullEventTypeThrowsNPE() {
            assertThatNullPointerException()
                    .isThrownBy(() -> registry.registerHandler(new TrackingHandler<>(), null))
                    .withMessageContaining("eventType must not be null");
        }

        @Test
        @DisplayName("Registering same event type twice throws DuplicateEventHandlerException")
        void duplicateRegistrationThrows() {
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);

            assertThatExceptionOfType(DuplicateEventHandlerException.class)
                    .isThrownBy(() -> registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class));
        }

        @Test
        @DisplayName("Multiple distinct event types can each have their own handler")
        void multipleDifferentTypesCanBeRegistered() {
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);
            registry.registerHandler(new TrackingHandler<PaymentCompletedEvent>(), PaymentCompletedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class)).isPresent();
            assertThat(registry.getHandlerForEvent(PaymentCompletedEvent.class)).isPresent();
        }

        @Test
        @DisplayName("First registration succeeds; original handler remains intact after duplicate attempt")
        void originalHandlerKeptAfterDuplicateAttempt() {
            var original = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(original, OrderCreatedEvent.class);

            assertThatExceptionOfType(DuplicateEventHandlerException.class)
                    .isThrownBy(() -> registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class));

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(original);
        }
    }

    @Nested
    @DisplayName("getHandlerForEvent")
    class GetHandlerForEvent {

        @Test
        @DisplayName("Returns empty Optional for a type with no registered handler")
        void returnsEmptyForUnregisteredType() {
            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class)).isEmpty();
        }

        @Test
        @DisplayName("Returns registered handler for an exact type match")
        void returnsHandlerForExactMatch() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(handler);
        }

        @Test
        @DisplayName("Returns parent handler when queried with a subclass that has no direct handler")
        void returnsParentHandlerForSubclass() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);

            // OrderShippedEvent extends OrderCreatedEvent — hierarchy walk should find it
            assertThat(registry.getHandlerForEvent(OrderShippedEvent.class))
                    .isPresent()
                    .containsSame(handler);
        }

        @Test
        @DisplayName("Prefers direct handler over inherited parent handler when both registered")
        void prefersDirektHandlerOverParent() {
            var parentHandler = new TrackingHandler<OrderCreatedEvent>();
            var childHandler = new TrackingHandler<OrderShippedEvent>();
            registry.registerHandler(parentHandler, OrderCreatedEvent.class);
            registry.registerHandler(childHandler, OrderShippedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderShippedEvent.class))
                    .isPresent()
                    .containsSame(childHandler);

            // Parent handler still accessible for the parent type
            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(parentHandler);
        }

        @Test
        @DisplayName("Returns empty Optional when no handler exists in the inheritance chain")
        void returnsEmptyWhenNoAncestorHandlerRegistered() {
            // Only PaymentCompletedEvent is registered; OrderCreatedEvent has no handler
            registry.registerHandler(new TrackingHandler<PaymentCompletedEvent>(), PaymentCompletedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class)).isEmpty();
        }

        @Test
        @DisplayName("Returns different handlers for different event types independently")
        void differentHandlersForDifferentTypes() {
            var orderHandler = new TrackingHandler<OrderCreatedEvent>();
            var paymentHandler = new TrackingHandler<PaymentCompletedEvent>();
            registry.registerHandler(orderHandler, OrderCreatedEvent.class);
            registry.registerHandler(paymentHandler, PaymentCompletedEvent.class);

            assertThat(registry.getHandlerForEvent(OrderCreatedEvent.class)).containsSame(orderHandler);
            assertThat(registry.getHandlerForEvent(PaymentCompletedEvent.class)).containsSame(paymentHandler);
        }
    }

    @Nested
    @DisplayName("Thread safety")
    class ThreadSafety {

        @Test
        @DisplayName("Concurrent reads after registration return consistent results")
        void concurrentReadsAreConsistent() throws InterruptedException {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);

            int threads = 16;
            var latch = new CountDownLatch(threads);
            var successCount = new AtomicInteger(0);
            var executor = Executors.newFixedThreadPool(threads);

            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    try {
                        var found = registry.getHandlerForEvent(OrderCreatedEvent.class);
                        if (found.isPresent() && found.get() == handler) {
                            successCount.incrementAndGet();
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
            assertThat(successCount.get()).isEqualTo(threads);
            executor.shutdown();
        }
    }
}
