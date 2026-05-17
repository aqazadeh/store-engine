package az.kon.academy.event.handler.autoconfiguration.metrics;

import az.kon.academy.event.handler.metric.EventHandlerMonitor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.ConcurrentHashMap;

public class DefaultEventHandlerMonitor implements EventHandlerMonitor {

    private static final String METRIC_PREFIX = "event.handler";

    private final MeterRegistry meterRegistry;

    private final ConcurrentHashMap<String, Counter> receivedCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> handlerNotFoundCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> successCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> failureCounters = new ConcurrentHashMap<>();


    public DefaultEventHandlerMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void reportReceived(String eventName) {
        this.receivedCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".received")
                                .description("Received events")
                                .tag("type", key)
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportHandlerNotFound(String eventName) {
        this.handlerNotFoundCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".not.found")
                                .description("Event handler not found")
                                .tag("type", key)
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportFailure(String eventName, Throwable cause) {
        this.failureCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".error")
                                .description("Event handling errors")
                                .tag("type", eventName)
                                .tag("exception", cause.getClass().getSimpleName())
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportSuccess(String eventName) {
        this.successCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".success")
                                .description("Successfully handled events")
                                .tag("type", eventName)
                                .register(meterRegistry)
                )
                .increment();
    }
}
