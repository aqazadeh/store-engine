package az.kon.academy.event.dispatcher;

import java.util.function.Supplier;

public interface EventDispatchMonitor {

    void reportDispatched(String eventName);

    void reportDispatcherNotFound(String eventName);

    void reportFailure(String eventName, String strategyName, Throwable cause);

    void reportSuccess(String eventName, String strategyName);

    <T> void record(String eventName, String strategyName, Supplier<T> supplier);

    EventDispatchMonitor NOOP = new EventDispatchMonitor() {
        @Override
        public void reportDispatched(String eventName) {
        }

        @Override
        public void reportDispatcherNotFound(String eventName) {
        }

        @Override
        public void reportFailure(String eventName, String strategyName, Throwable cause) {
        }

        @Override
        public void reportSuccess(String eventName, String strategyName) {
        }

        @Override
        public <T> void record(String eventName, String strategyName, Supplier<T> supplier) {
            supplier.get();
        }
    };
}
