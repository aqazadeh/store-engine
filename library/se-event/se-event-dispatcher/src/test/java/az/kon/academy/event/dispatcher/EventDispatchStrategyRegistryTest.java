package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import az.kon.academy.event.dispatcher.exception.EventDispatchRegistryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

class EventDispatchStrategyRegistryTest {

    // --- Fixtures ---

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        OrderCreatedEvent() { super("order-1", OffsetDateTime.now()); }
    }

    @Event(version = 1)
    static class OrderShippedEvent extends OrderCreatedEvent {
        // subclass — used for inheritance lookup tests
    }

    @Event(version = 1)
    static class PaymentCompletedEvent extends DomainEvent {
        PaymentCompletedEvent() { super("payment-1", OffsetDateTime.now()); }
    }

    static class NoopStrategy implements EventDispatchStrategy {
        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {}
    }

    // --- Setup ---

    private EventDispatchStrategyRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new EventDispatchStrategyRegistry();
    }

    // --- Tests ---

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("Null eventType throws EventDispatchRegistryException")
        void nullEventTypeThrows() {
            assertThatExceptionOfType(EventDispatchRegistryException.class)
                    .isThrownBy(() -> registry.register(null, new NoopStrategy()));
        }

        @Test
        @DisplayName("Null strategy throws EventDispatchRegistryException")
        void nullStrategyThrows() {
            assertThatExceptionOfType(EventDispatchRegistryException.class)
                    .isThrownBy(() -> registry.register(OrderCreatedEvent.class, null));
        }

        @Test
        @DisplayName("Successful registration makes strategy retrievable via get()")
        void successfulRegistrationMakesStrategyRetrievable() {
            var strategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, strategy);

            assertThat(registry.get(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(strategy);
        }

        @Test
        @DisplayName("Registering the same type twice without overwrite throws EventDispatchRegistryException")
        void duplicateRegistrationThrows() {
            registry.register(OrderCreatedEvent.class, new NoopStrategy());

            assertThatExceptionOfType(EventDispatchRegistryException.class)
                    .isThrownBy(() -> registry.register(OrderCreatedEvent.class, new NoopStrategy()));
        }

        @Test
        @DisplayName("Original strategy is preserved after a failed duplicate registration attempt")
        void originalStrategyKeptAfterDuplicateAttempt() {
            var original = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, original);

            assertThatExceptionOfType(EventDispatchRegistryException.class)
                    .isThrownBy(() -> registry.register(OrderCreatedEvent.class, new NoopStrategy()));

            assertThat(registry.get(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(original);
        }

        @Test
        @DisplayName("overwrite=true replaces the previously registered strategy")
        void overwriteTrueReplacesExistingStrategy() {
            var original = new NoopStrategy();
            var replacement = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, original);

            registry.register(OrderCreatedEvent.class, replacement, true);

            assertThat(registry.get(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(replacement);
        }

        @Test
        @DisplayName("overwrite=false on a duplicate still throws and does not replace")
        void overwriteFalseOnDuplicateThrowsAndDoesNotReplace() {
            var original = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, original);

            assertThatExceptionOfType(EventDispatchRegistryException.class)
                    .isThrownBy(() -> registry.register(OrderCreatedEvent.class, new NoopStrategy(), false));

            assertThat(registry.get(OrderCreatedEvent.class))
                    .containsSame(original);
        }

        @Test
        @DisplayName("Multiple distinct event types can each have their own strategy")
        void multipleDistinctTypesRegisteredIndependently() {
            var orderStrategy = new NoopStrategy();
            var paymentStrategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, orderStrategy);
            registry.register(PaymentCompletedEvent.class, paymentStrategy);

            assertThat(registry.get(OrderCreatedEvent.class)).containsSame(orderStrategy);
            assertThat(registry.get(PaymentCompletedEvent.class)).containsSame(paymentStrategy);
        }
    }

    @Nested
    @DisplayName("get")
    class Get {

        @Test
        @DisplayName("Returns empty Optional for an unregistered event type")
        void returnsEmptyForUnregisteredType() {
            assertThat(registry.get(OrderCreatedEvent.class)).isEmpty();
        }

        @Test
        @DisplayName("Returns strategy for an exact event type match")
        void returnsStrategyForExactTypeMatch() {
            var strategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, strategy);

            assertThat(registry.get(OrderCreatedEvent.class))
                    .isPresent()
                    .containsSame(strategy);
        }

        @Test
        @DisplayName("Returns parent strategy when the child type has no direct registration")
        void returnsParentStrategyForUnregisteredSubclass() {
            var strategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, strategy);

            assertThat(registry.get(OrderShippedEvent.class))
                    .isPresent()
                    .containsSame(strategy);
        }

        @Test
        @DisplayName("Direct strategy takes precedence over an inherited parent strategy")
        void directStrategyTakesPrecedenceOverInherited() {
            var parentStrategy = new NoopStrategy();
            var childStrategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, parentStrategy);
            registry.register(OrderShippedEvent.class, childStrategy);

            assertThat(registry.get(OrderShippedEvent.class)).containsSame(childStrategy);
            assertThat(registry.get(OrderCreatedEvent.class)).containsSame(parentStrategy);
        }

        @Test
        @DisplayName("Returns empty when no ancestor in the hierarchy has a registered strategy")
        void returnsEmptyWhenNoAncestorStrategyExists() {
            registry.register(PaymentCompletedEvent.class, new NoopStrategy());

            assertThat(registry.get(OrderCreatedEvent.class)).isEmpty();
        }

        @Test
        @DisplayName("Repeated get() calls for the same type return the same strategy instance (ClassValue cache)")
        void repeatedGetCallsReturnSameInstance() {
            var strategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, strategy);

            var first = registry.get(OrderCreatedEvent.class);
            var second = registry.get(OrderCreatedEvent.class);

            assertThat(first).isPresent();
            assertThat(second).isPresent();
            assertThat(first.get()).isSameAs(second.get());
        }
    }

    @Nested
    @DisplayName("Thread safety")
    class ThreadSafety {

        @Test
        @DisplayName("Concurrent reads after registration always return the correct strategy")
        void concurrentReadsAfterRegistrationAreConsistent() throws InterruptedException {
            var strategy = new NoopStrategy();
            registry.register(OrderCreatedEvent.class, strategy);

            int threads = 16;
            var latch = new CountDownLatch(threads);
            var successCount = new AtomicInteger(0);
            var executor = Executors.newFixedThreadPool(threads);

            for (int i = 0; i < threads; i++) {
                executor.submit(() -> {
                    try {
                        var found = registry.get(OrderCreatedEvent.class);
                        if (found.isPresent() && found.get() == strategy) {
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
