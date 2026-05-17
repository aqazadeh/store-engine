package az.kon.academy.event.dispatcher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.*;

class EventDispatchMonitorTest {

    private static final String EVENT_NAME = "OrderCreatedEvent";
    private static final String STRATEGY_NAME = "KafkaStrategy";

    @Nested
    @DisplayName("NOOP implementation")
    class Noop {

        private final EventDispatchMonitor noop = EventDispatchMonitor.NOOP;

        @Test
        @DisplayName("NOOP is an instance of NoopEventDispatchMonitor")
        void noopIsCorrectType() {
            assertThat(noop).isInstanceOf(EventDispatchMonitor.NoopEventDispatchMonitor.class);
        }

        @Test
        @DisplayName("reportDispatched does not throw")
        void reportDispatchedDoesNotThrow() {
            assertThatNoException().isThrownBy(() -> noop.reportDispatched(EVENT_NAME));
        }

        @Test
        @DisplayName("reportDispatcherNotFound does not throw")
        void reportDispatcherNotFoundDoesNotThrow() {
            assertThatNoException().isThrownBy(() -> noop.reportDispatcherNotFound(EVENT_NAME));
        }

        @Test
        @DisplayName("reportSuccess does not throw")
        void reportSuccessDoesNotThrow() {
            assertThatNoException().isThrownBy(() -> noop.reportSuccess(EVENT_NAME, STRATEGY_NAME));
        }

        @Test
        @DisplayName("reportFailure does not throw")
        void reportFailureDoesNotThrow() {
            assertThatNoException()
                    .isThrownBy(() -> noop.reportFailure(EVENT_NAME, STRATEGY_NAME, new RuntimeException("cause")));
        }

        @Test
        @DisplayName("record() invokes the supplier")
        void recordInvokesSupplier() {
            var invoked = new AtomicBoolean(false);

            noop.record(EVENT_NAME, STRATEGY_NAME, () -> {
                invoked.set(true);
                return null;
            });

            assertThat(invoked.get()).isTrue();
        }

        @Test
        @DisplayName("record() propagates exceptions thrown by the supplier")
        void recordPropagatesSupplierException() {
            var cause = new RuntimeException("supplier failed");

            assertThatException()
                    .isThrownBy(() -> noop.record(EVENT_NAME, STRATEGY_NAME, () -> { throw cause; }))
                    .isSameAs(cause);
        }

        @Test
        @DisplayName("NOOP constant is always the same instance")
        void noopIsSingleton() {
            assertThat(EventDispatchMonitor.NOOP).isSameAs(EventDispatchMonitor.NOOP);
        }
    }
}
