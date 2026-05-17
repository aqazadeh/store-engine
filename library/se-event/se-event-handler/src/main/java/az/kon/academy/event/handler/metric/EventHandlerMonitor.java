package az.kon.academy.event.handler.metric;

public interface EventHandlerMonitor {
    void reportReceived(String eventName);

    void reportHandlerNotFound(String eventName);

    void reportFailure(String eventName, Throwable cause);

    void reportSuccess(String eventName);

    EventHandlerMonitor NOOP = new EventHandlerMonitor() {

        @Override
        public void reportReceived(String eventName) {}

        @Override
        public void reportHandlerNotFound(String eventName) {}

        @Override
        public void reportFailure(String eventName, Throwable cause) {}

        @Override
        public void reportSuccess(String eventName) {}

    };
}
