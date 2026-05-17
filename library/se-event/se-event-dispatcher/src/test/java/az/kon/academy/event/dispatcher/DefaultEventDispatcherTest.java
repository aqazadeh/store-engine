package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

class DefaultEventDispatcherTest {

    // --- Fixtures ---

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        OrderCreatedEvent() { super("order-1", OffsetDateTime.now()); }
        OrderCreatedEvent(String aggregateId) { super(aggregateId, OffsetDateTime.now()); }
    }

    @Event(version = 1)
    static class PaymentCompletedEvent extends DomainEvent {
        PaymentCompletedEvent() { super("payment-1", OffsetDateTime.now()); }
    }

    static class TrackingStrategy implements EventDispatchStrategy {
        int callCount;
        final List<AbstractEvent> capturedPayloads = new ArrayList<>();

        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
            callCount++;
            eventMessages.forEach(m -> capturedPayloads.add(m.getPayload()));
        }
    }

    static class ThrowingStrategy implements EventDispatchStrategy {
        private final RuntimeException toThrow;

        ThrowingStrategy(RuntimeException toThrow) { this.toThrow = toThrow; }

        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
            throw toThrow;
        }
    }

    static class TrackingMonitor implements EventDispatchMonitor {
        int dispatchedCount;
        int notFoundCount;
        int successCount;
        int failureCount;
        Throwable lastFailureCause;

        @Override
        public void reportDispatched(String eventName) { dispatchedCount++; }

        @Override
        public void reportDispatcherNotFound(String eventName) { notFoundCount++; }

        @Override
        public void reportSuccess(String eventName, String strategyName) { successCount++; }

        @Override
        public void reportFailure(String eventName, String strategyName, Throwable cause) {
            failureCount++;
            lastFailureCause = cause;
        }

        @Override
        public <T> void record(String eventName, String strategyName, Supplier<T> supplier) {
            supplier.get();
        }
    }

    // --- Setup ---

    private EventDispatchStrategyRegistry strategyRegistry;
    private TrackingMonitor monitor;
    private DefaultEventDispatcher dispatcher;

    private static final UUID CORRELATION_ID = UUID.randomUUID();
    private static final UUID CAUSATION_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        strategyRegistry = new EventDispatchStrategyRegistry();
        monitor = new TrackingMonitor();
        dispatcher = new DefaultEventDispatcher(strategyRegistry, monitor);
    }

    // --- Tests ---

    @Nested
    @DisplayName("Null and empty input")
    class NullAndEmpty {

        @Test
        @DisplayName("Null list does nothing and throws no exception")
        void nullListDoesNothing() {
            assertThatNoException()
                    .isThrownBy(() -> dispatcher.dispatch((List<AbstractEvent>) null, CORRELATION_ID, CAUSATION_ID));
            assertThat(monitor.dispatchedCount).isZero();
        }

        @Test
        @DisplayName("Empty list does nothing and throws no exception")
        void emptyListDoesNothing() {
            assertThatNoException()
                    .isThrownBy(() -> dispatcher.dispatch(List.of(), CORRELATION_ID, CAUSATION_ID));
            assertThat(monitor.dispatchedCount).isZero();
        }
    }

    @Nested
    @DisplayName("Strategy dispatch")
    class StrategyDispatch {

        @Test
        @DisplayName("Single event is forwarded to its registered strategy")
        void singleEventForwardedToStrategy() {
            var strategy = new TrackingStrategy();
            strategyRegistry.register(OrderCreatedEvent.class, strategy);
            var event = new OrderCreatedEvent();

            dispatcher.dispatch(List.of(event), CORRELATION_ID, CAUSATION_ID);

            assertThat(strategy.callCount).isOne();
            assertThat(strategy.capturedPayloads).containsExactly(event);
        }

        @Test
        @DisplayName("Multiple events sharing the same strategy are passed in a single proceed() call")
        void sameStrategyEventsBatchedIntoSingleProceedCall() {
            var strategy = new TrackingStrategy();
            strategyRegistry.register(OrderCreatedEvent.class, strategy);
            var e1 = new OrderCreatedEvent("order-1");
            var e2 = new OrderCreatedEvent("order-2");
            var e3 = new OrderCreatedEvent("order-3");

            dispatcher.dispatch(List.of(e1, e2, e3), CORRELATION_ID, CAUSATION_ID);

            assertThat(strategy.callCount).isOne();
            assertThat(strategy.capturedPayloads).containsExactlyInAnyOrder(e1, e2, e3);
        }

        @Test
        @DisplayName("Events of different types are each dispatched to their own strategy")
        void differentTypesDispatchedToRespectiveStrategies() {
            var orderStrategy = new TrackingStrategy();
            var paymentStrategy = new TrackingStrategy();
            strategyRegistry.register(OrderCreatedEvent.class, orderStrategy);
            strategyRegistry.register(PaymentCompletedEvent.class, paymentStrategy);

            var order = new OrderCreatedEvent();
            var payment = new PaymentCompletedEvent();

            dispatcher.dispatch(List.of(order, payment), CORRELATION_ID, CAUSATION_ID);

            assertThat(orderStrategy.capturedPayloads).containsExactly(order);
            assertThat(paymentStrategy.capturedPayloads).containsExactly(payment);
        }

        @Test
        @DisplayName("EventMessage header carries the correct correlationId and causationId")
        void eventMessageHeaderCarriesCorrectIds() {
            var strategy = new CapturingMessageStrategy();
            strategyRegistry.register(OrderCreatedEvent.class, strategy);

            dispatcher.dispatch(List.of(new OrderCreatedEvent()), CORRELATION_ID, CAUSATION_ID);

            var header = strategy.capturedMessages.getFirst().getHeader();
            assertThat(header.getCorrelationId()).isEqualTo(CORRELATION_ID);
            assertThat(header.getCausationId()).isEqualTo(CAUSATION_ID);
        }
    }

    @Nested
    @DisplayName("No strategy registered")
    class NoStrategy {

        @Test
        @DisplayName("Event with no registered strategy is silently skipped")
        void eventWithNoStrategySkippedSilently() {
            assertThatNoException()
                    .isThrownBy(() -> dispatcher.dispatch(List.of(new OrderCreatedEvent()), CORRELATION_ID, CAUSATION_ID));
        }

        @Test
        @DisplayName("reportDispatcherNotFound is called once for each unregistered event type")
        void reportDispatcherNotFoundCalledForUnregisteredEvent() {
            dispatcher.dispatch(
                    List.of(new OrderCreatedEvent(), new OrderCreatedEvent()),
                    CORRELATION_ID, CAUSATION_ID);

            assertThat(monitor.notFoundCount).isEqualTo(2);
        }

        @Test
        @DisplayName("Registered events are still dispatched when mixed with unregistered events")
        void registeredEventsDispatchedWhenMixedWithUnregistered() {
            var strategy = new TrackingStrategy();
            strategyRegistry.register(PaymentCompletedEvent.class, strategy);

            var payment = new PaymentCompletedEvent();
            dispatcher.dispatch(List.of(new OrderCreatedEvent(), payment), CORRELATION_ID, CAUSATION_ID);

            assertThat(strategy.capturedPayloads).containsExactly(payment);
            assertThat(monitor.notFoundCount).isOne();
        }
    }

    @Nested
    @DisplayName("Monitor integration")
    class MonitorIntegration {

        @Test
        @DisplayName("reportDispatched is called once per event regardless of strategy presence")
        void reportDispatchedCalledOncePerEvent() {
            strategyRegistry.register(OrderCreatedEvent.class, new TrackingStrategy());

            dispatcher.dispatch(
                    List.of(new OrderCreatedEvent(), new OrderCreatedEvent(), new OrderCreatedEvent()),
                    CORRELATION_ID, CAUSATION_ID);

            assertThat(monitor.dispatchedCount).isEqualTo(3);
        }

        @Test
        @DisplayName("reportSuccess is called once per successfully executed strategy group")
        void reportSuccessCalledPerStrategyGroup() {
            strategyRegistry.register(OrderCreatedEvent.class, new TrackingStrategy());
            strategyRegistry.register(PaymentCompletedEvent.class, new TrackingStrategy());

            dispatcher.dispatch(
                    List.of(new OrderCreatedEvent(), new PaymentCompletedEvent()),
                    CORRELATION_ID, CAUSATION_ID);

            assertThat(monitor.successCount).isEqualTo(2);
            assertThat(monitor.failureCount).isZero();
        }

        @Test
        @DisplayName("reportFailure is called with the original cause when a strategy throws")
        void reportFailureCalledWithOriginalCause() {
            var cause = new RuntimeException("strategy failure");
            strategyRegistry.register(OrderCreatedEvent.class, new ThrowingStrategy(cause));

            assertThatException()
                    .isThrownBy(() -> dispatcher.dispatch(List.of(new OrderCreatedEvent()), CORRELATION_ID, CAUSATION_ID));

            assertThat(monitor.failureCount).isOne();
            assertThat(monitor.lastFailureCause).isSameAs(cause);
        }

        @Test
        @DisplayName("reportSuccess is not called when the strategy throws")
        void reportSuccessNotCalledOnFailure() {
            strategyRegistry.register(OrderCreatedEvent.class, new ThrowingStrategy(new RuntimeException()));

            assertThatException()
                    .isThrownBy(() -> dispatcher.dispatch(List.of(new OrderCreatedEvent()), CORRELATION_ID, CAUSATION_ID));

            assertThat(monitor.successCount).isZero();
        }

        @Test
        @DisplayName("Monitor is not called at all for null or empty lists")
        void monitorNotCalledForNullOrEmptyList() {
            dispatcher.dispatch((List<AbstractEvent>) null, CORRELATION_ID, CAUSATION_ID);
            dispatcher.dispatch(List.<AbstractEvent>of(), CORRELATION_ID, CAUSATION_ID);

            assertThat(monitor.dispatchedCount).isZero();
            assertThat(monitor.successCount).isZero();
        }
    }

    @Nested
    @DisplayName("Error handling")
    class ErrorHandling {

        @Test
        @DisplayName("Exception thrown by a strategy propagates unchanged to the caller")
        void strategyExceptionPropagatesUnchanged() {
            var cause = new RuntimeException("proceed failure");
            strategyRegistry.register(OrderCreatedEvent.class, new ThrowingStrategy(cause));

            assertThatException()
                    .isThrownBy(() -> dispatcher.dispatch(List.of(new OrderCreatedEvent()), CORRELATION_ID, CAUSATION_ID))
                    .isSameAs(cause);
        }
    }

    @Nested
    @DisplayName("dispatch(single event) — default interface method")
    class SingleEventDefaultMethod {

        @Test
        @DisplayName("Single-event overload delegates to the list-based dispatch")
        void singleEventDelegatesToListDispatch() {
            var strategy = new TrackingStrategy();
            strategyRegistry.register(OrderCreatedEvent.class, strategy);
            var event = new OrderCreatedEvent();

            dispatcher.dispatch(event, CORRELATION_ID, CAUSATION_ID);

            assertThat(strategy.capturedPayloads).containsExactly(event);
        }

        @Test
        @DisplayName("reportDispatched is still called when using the single-event overload")
        void reportDispatchedCalledForSingleEventOverload() {
            strategyRegistry.register(OrderCreatedEvent.class, new TrackingStrategy());

            dispatcher.dispatch(new OrderCreatedEvent(), CORRELATION_ID, CAUSATION_ID);

            assertThat(monitor.dispatchedCount).isOne();
        }
    }

    // helper to capture EventMessage instances (needed for header assertions)
    static class CapturingMessageStrategy implements EventDispatchStrategy {
        final List<EventMessage<?>> capturedMessages = new ArrayList<>();

        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
            capturedMessages.addAll(eventMessages);
        }
    }
}
