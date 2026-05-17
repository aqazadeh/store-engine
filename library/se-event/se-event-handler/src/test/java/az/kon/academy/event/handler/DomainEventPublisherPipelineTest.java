package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import az.kon.academy.event.handler.exception.EventHandlerNotFoundException;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.metric.EventHandlerMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class DomainEventPublisherPipelineTest {

    // --- Fixtures ---

    @Event(version = 1)
    static class OrderCreatedEvent extends DomainEvent {
        OrderCreatedEvent(String aggregateId) {
            super(aggregateId, OffsetDateTime.now());
        }
    }

    @Event(version = 1)
    static class PaymentCompletedEvent extends DomainEvent {
        PaymentCompletedEvent(String aggregateId) {
            super(aggregateId, OffsetDateTime.now());
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

    static class ThrowingHandler<E extends AbstractEvent> implements BaseEventHandler<E> {
        private final RuntimeException toThrow;

        ThrowingHandler(RuntimeException toThrow) {
            this.toThrow = toThrow;
        }

        @Override
        public void handle(E event) {
            throw toThrow;
        }
    }

    static class TrackingInterceptor implements EventHandlerInterceptor {
        final List<String> calls = new ArrayList<>();
        AbstractEvent lastEvent;
        Exception lastError;

        @Override
        public <T extends AbstractEvent> void onReceive(T event) {
            calls.add("onReceive");
            lastEvent = event;
        }

        @Override
        public <T extends AbstractEvent> void preExecution(T event) {
            calls.add("preExecution");
        }

        @Override
        public <T extends AbstractEvent> void postExecution(T event) {
            calls.add("postExecution");
        }

        @Override
        public <T extends AbstractEvent> void onComplete(T event) {
            calls.add("onComplete");
        }

        @Override
        public <T extends AbstractEvent> void onError(T event, Exception e) {
            calls.add("onError");
            lastError = e;
        }
    }

    static class TrackingMonitor implements EventHandlerMonitor {
        int receivedCount;
        int handlerNotFoundCount;
        int successCount;
        int failureCount;
        String lastEventName;
        Throwable lastCause;

        @Override
        public void reportReceived(String eventName) {
            receivedCount++;
            lastEventName = eventName;
        }

        @Override
        public void reportHandlerNotFound(String eventName) {
            handlerNotFoundCount++;
        }

        @Override
        public void reportFailure(String eventName, Throwable cause) {
            failureCount++;
            lastCause = cause;
        }

        @Override
        public void reportSuccess(String eventName) {
            successCount++;
        }
    }

    static class StubRegistry implements EventHandlerRegistry {
        private final Map<Class<? extends AbstractEvent>, BaseEventHandler<? extends AbstractEvent>> handlers = new HashMap<>();

        @Override
        public Optional<BaseEventHandler<?>> getHandlerForEvent(Class<? extends AbstractEvent> eventType) {
            return Optional.ofNullable(handlers.get(eventType));
        }

        @Override
        public void registerHandler(BaseEventHandler<? extends AbstractEvent> handler, Class<? extends AbstractEvent> eventType) {
            handlers.put(eventType, handler);
        }
    }

    // --- Setup ---

    private StubRegistry registry;
    private TrackingMonitor monitor;
    private DomainEventPublisherPipeline pipeline;

    @BeforeEach
    void setUp() {
        registry = new StubRegistry();
        monitor = new TrackingMonitor();
        pipeline = new DomainEventPublisherPipeline(registry, monitor);
    }

    // --- Tests ---

    @Nested
    @DisplayName("publish(E event) — basic behaviour")
    class PublishSingleEvent {

        @Test
        @DisplayName("publish(null) does nothing and throws no exception")
        void publishNullDoesNothing() {
            assertThatNoException().isThrownBy(() -> pipeline.publish((AbstractEvent) null));
            assertThat(monitor.receivedCount).isEqualTo(0);
        }

        @Test
        @DisplayName("Throws EventHandlerNotFoundException when no handler is registered")
        void throwsWhenNoHandlerRegistered() {
            var event = new OrderCreatedEvent("order-1");

            assertThatExceptionOfType(EventHandlerNotFoundException.class)
                    .isThrownBy(() -> pipeline.publish(event));
        }

        @Test
        @DisplayName("Calls handle() with the correct event when handler is registered")
        void callsHandlerWithCorrectEvent() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);
            var event = new OrderCreatedEvent("order-1");

            pipeline.publish(event);

            assertThat(handler.handled()).containsExactly(event);
        }

        @Test
        @DisplayName("Exception thrown by the handler propagates to the caller")
        void handlerExceptionPropagates() {
            var cause = new RuntimeException("handler failure");
            registry.registerHandler(new ThrowingHandler<OrderCreatedEvent>(cause), OrderCreatedEvent.class);

            assertThatException()
                    .isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")))
                    .isSameAs(cause);
        }

        @Test
        @DisplayName("Consecutive events are handled independently after a successful publish")
        void consecutiveEventsHandledIndependently() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);
            var event1 = new OrderCreatedEvent("order-1");
            var event2 = new OrderCreatedEvent("order-2");

            pipeline.publish(event1);
            pipeline.publish(event2);

            assertThat(handler.handled()).containsExactly(event1, event2);
        }
    }

    @Nested
    @DisplayName("Monitor integration")
    class MonitorIntegration {

        @Test
        @DisplayName("reportReceived is triggered on every publish and carries the event name")
        void reportReceivedCalledWithEventName() {
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);
            pipeline.publish(new OrderCreatedEvent("order-1"));

            assertThat(monitor.receivedCount).isEqualTo(1);
            assertThat(monitor.lastEventName).isEqualTo("OrderCreatedEvent");
        }

        @Test
        @DisplayName("reportSuccess is called after successful handling")
        void reportSuccessCalledOnSuccess() {
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);
            pipeline.publish(new OrderCreatedEvent("order-1"));

            assertThat(monitor.successCount).isEqualTo(1);
            assertThat(monitor.failureCount).isEqualTo(0);
        }

        @Test
        @DisplayName("reportHandlerNotFound and reportFailure are called when no handler is found")
        void reportHandlerNotFoundAndFailureCalledWhenNoHandler() {
            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(monitor.handlerNotFoundCount).isEqualTo(1);
            assertThat(monitor.failureCount).isEqualTo(1);
            assertThat(monitor.successCount).isEqualTo(0);
        }

        @Test
        @DisplayName("reportFailure is called with the correct cause when the handler throws")
        void reportFailureCalledWithCorrectCause() {
            var cause = new RuntimeException("boom");
            registry.registerHandler(new ThrowingHandler<OrderCreatedEvent>(cause), OrderCreatedEvent.class);

            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(monitor.failureCount).isEqualTo(1);
            assertThat(monitor.lastCause).isSameAs(cause);
            assertThat(monitor.successCount).isEqualTo(0);
        }

        @Test
        @DisplayName("Monitor is never triggered when a null event is published")
        void monitorNotCalledForNullEvent() {
            pipeline.publish((AbstractEvent) null);

            assertThat(monitor.receivedCount).isEqualTo(0);
        }

        @Test
        @DisplayName("reportReceived is called separately for each published event")
        void reportReceivedCalledForEachEvent() {
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);
            pipeline.publish(new OrderCreatedEvent("order-1"));
            pipeline.publish(new OrderCreatedEvent("order-2"));

            assertThat(monitor.receivedCount).isEqualTo(2);
            assertThat(monitor.successCount).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Interceptor lifecycle")
    class InterceptorLifecycle {

        @Test
        @DisplayName("On the success path, interceptor methods are called in the correct order")
        void successPathCallsInterceptorsInOrder() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);

            pipeline.publish(new OrderCreatedEvent("order-1"));

            assertThat(interceptor.calls)
                    .containsExactly("onReceive", "preExecution", "postExecution", "onComplete");
        }

        @Test
        @DisplayName("When no handler found: onReceive → onError → onComplete; preExecution is never called")
        void noHandlerPathCallsOnReceiveThenOnError() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);

            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(interceptor.calls)
                    .containsExactly("onReceive", "onError", "onComplete")
                    .doesNotContain("preExecution", "postExecution");
        }

        @Test
        @DisplayName("When handler throws: onReceive → preExecution → onError → onComplete; postExecution is skipped")
        void handlerThrowsPathSkipsPostExecution() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);
            registry.registerHandler(
                    new ThrowingHandler<OrderCreatedEvent>(new RuntimeException("boom")),
                    OrderCreatedEvent.class
            );

            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(interceptor.calls)
                    .containsExactly("onReceive", "preExecution", "onError", "onComplete")
                    .doesNotContain("postExecution");
        }

        @Test
        @DisplayName("onError receives the original exception")
        void onErrorReceivesOriginalException() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);
            var cause = new RuntimeException("specific error");
            registry.registerHandler(new ThrowingHandler<OrderCreatedEvent>(cause), OrderCreatedEvent.class);

            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(interceptor.lastError).isSameAs(cause);
        }

        @Test
        @DisplayName("onComplete is always called via the finally block regardless of handler outcome")
        void onCompleteAlwaysCalledViaFinally() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);
            registry.registerHandler(
                    new ThrowingHandler<OrderCreatedEvent>(new RuntimeException()),
                    OrderCreatedEvent.class
            );

            assertThatException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(interceptor.calls).contains("onComplete");
        }

        @Test
        @DisplayName("An exception thrown in interceptor onReceive does not stop the pipeline")
        void interceptorExceptionInOnReceiveDoesNotStopPipeline() {
            var trackingHandler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(trackingHandler, OrderCreatedEvent.class);
            pipeline.registerInterceptor(new EventHandlerInterceptor() {
                @Override
                public <T extends AbstractEvent> void onReceive(T event) {
                    throw new RuntimeException("interceptor blown up");
                }
            });
            var event = new OrderCreatedEvent("order-1");

            assertThatNoException().isThrownBy(() -> pipeline.publish(event));
            assertThat(trackingHandler.handled()).containsExactly(event);
        }

        @Test
        @DisplayName("An exception thrown in interceptor postExecution does not skip subsequent interceptors")
        void interceptorExceptionInPostExecutionDoesNotSkipOthers() {
            var second = new TrackingInterceptor();
            pipeline.registerInterceptor(new EventHandlerInterceptor() {
                @Override
                public <T extends AbstractEvent> void postExecution(T event) {
                    throw new RuntimeException("post exec failure");
                }
            });
            pipeline.registerInterceptor(second);
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);

            assertThatNoException().isThrownBy(() -> pipeline.publish(new OrderCreatedEvent("order-1")));

            assertThat(second.calls).contains("postExecution");
        }

        @Test
        @DisplayName("All registered interceptors are called")
        void allRegisteredInterceptorsAreCalled() {
            var first = new TrackingInterceptor();
            var second = new TrackingInterceptor();
            var third = new TrackingInterceptor();
            pipeline.registerInterceptor(first);
            pipeline.registerInterceptor(second);
            pipeline.registerInterceptor(third);
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);

            pipeline.publish(new OrderCreatedEvent("order-1"));

            assertThat(first.calls).containsExactly("onReceive", "preExecution", "postExecution", "onComplete");
            assertThat(second.calls).containsExactly("onReceive", "preExecution", "postExecution", "onComplete");
            assertThat(third.calls).containsExactly("onReceive", "preExecution", "postExecution", "onComplete");
        }

        @Test
        @DisplayName("Interceptor receives the correct event instance")
        void interceptorReceivesCorrectEventInstance() {
            var interceptor = new TrackingInterceptor();
            pipeline.registerInterceptor(interceptor);
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);
            var event = new OrderCreatedEvent("order-1");

            pipeline.publish(event);

            assertThat(interceptor.lastEvent).isSameAs(event);
        }
    }

    @Nested
    @DisplayName("publish(List<E> events) — default method")
    class PublishEventList {

        @Test
        @DisplayName("All events in the list are forwarded to the handler")
        void publishesAllEventsInList() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);
            var event1 = new OrderCreatedEvent("order-1");
            var event2 = new OrderCreatedEvent("order-2");
            var event3 = new OrderCreatedEvent("order-3");

            pipeline.publish(List.of(event1, event2, event3));

            assertThat(handler.handled()).containsExactly(event1, event2, event3);
        }

        @Test
        @DisplayName("Publishing an empty list does nothing")
        void emptyListDoesNothing() {
            assertThatNoException()
                    .isThrownBy(() -> pipeline.publish(List.<OrderCreatedEvent>of()));
            assertThat(monitor.receivedCount).isEqualTo(0);
        }

        @Test
        @DisplayName("Processing stops when an event in the list throws an exception")
        void stopsOnFirstExceptionInList() {
            var handler = new TrackingHandler<OrderCreatedEvent>();
            registry.registerHandler(handler, OrderCreatedEvent.class);

            var good = new OrderCreatedEvent("order-good");
            // No handler for PaymentCompletedEvent — EventHandlerNotFoundException is thrown
            var bad = new PaymentCompletedEvent("payment-bad");
            var neverReached = new OrderCreatedEvent("order-never");

            // publish(List) uses forEach; iteration stops when the second element throws
            assertThatExceptionOfType(EventHandlerNotFoundException.class)
                    .isThrownBy(() -> pipeline.publish(List.of(good, bad, neverReached)));

            assertThat(handler.handled()).containsExactly(good);
        }
    }

    @Nested
    @DisplayName("Constructor")
    class Construction {

        @Test
        @DisplayName("No-monitor constructor uses NOOP monitor and works normally")
        void constructorWithoutMonitorUsesNoop() {
            var pipelineNoMonitor = new DomainEventPublisherPipeline(registry);
            registry.registerHandler(new TrackingHandler<OrderCreatedEvent>(), OrderCreatedEvent.class);

            assertThatNoException()
                    .isThrownBy(() -> pipelineNoMonitor.publish(new OrderCreatedEvent("order-1")));
        }

        @Test
        @DisplayName("No-monitor constructor still throws when no handler is found")
        void constructorWithoutMonitorStillThrowsOnMissingHandler() {
            var pipelineNoMonitor = new DomainEventPublisherPipeline(registry);

            assertThatExceptionOfType(EventHandlerNotFoundException.class)
                    .isThrownBy(() -> pipelineNoMonitor.publish(new OrderCreatedEvent("order-1")));
        }
    }
}
