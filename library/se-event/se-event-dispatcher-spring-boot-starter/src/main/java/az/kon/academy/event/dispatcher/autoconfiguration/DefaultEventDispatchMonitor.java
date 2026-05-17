package az.kon.academy.event.dispatcher.autoconfiguration;

import az.kon.academy.event.dispatcher.EventDispatchMonitor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DefaultEventDispatchMonitor implements EventDispatchMonitor {

    private static final String METRIC_PREFIX = "event.dispatch";
    private static final String EVENT_NAME_TAG = "event_name";
    private static final String STRATEGY_TAG = "strategy";
    private static final String OUTCOME_TAG = "outcome";
    private static final String EXCEPTION_TAG = "exception";
    private static final String OUTCOME_SUCCESS = "success";
    private static final String OUTCOME_FAILURE = "failure";

    private final MeterRegistry meterRegistry;


    private final ConcurrentHashMap<String, Counter> dispatchedCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> dispatchedNotFoundCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> successCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Counter> failureCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timerMap = new ConcurrentHashMap<>();

    public DefaultEventDispatchMonitor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void reportDispatched(String eventName) {
        dispatchedCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".dispatcher.dispatched")
                                .description("Events dispatched")
                                .tag(EVENT_NAME_TAG, key)
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportDispatcherNotFound(String eventName) {
        dispatchedNotFoundCounters
                .computeIfAbsent(eventName, key ->
                        Counter.builder(METRIC_PREFIX + ".dispatcher.not.found")
                                .description("Event dispatcher not found")
                                .tag(EVENT_NAME_TAG, key)
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportFailure(String eventName, String strategyName, Throwable cause) {
        String key = eventName + ":" + strategyName + ":" + cause.getClass().getSimpleName();

        failureCounters
                .computeIfAbsent(key, k ->
                        Counter.builder(METRIC_PREFIX + ".failure")
                                .description("Event dispatch failures")
                                .tag(STRATEGY_TAG, strategyName)
                                .tag(EVENT_NAME_TAG, eventName)
                                .tag(EXCEPTION_TAG, cause.getClass().getSimpleName())
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public void reportSuccess(String eventName, String strategyName) {
        String key = strategyName + ":" + eventName;
        successCounters
                .computeIfAbsent(key, k ->
                        Counter.builder(METRIC_PREFIX + ".success")
                                .description("Successful event dispatches")
                                .tag(STRATEGY_TAG, strategyName)
                                .tag(EVENT_NAME_TAG, eventName)
                                .register(meterRegistry)
                )
                .increment();
    }

    @Override
    public <T> void record(String eventName, String strategyName, Supplier<T> consumer) {
        long start = System.nanoTime();
        try {
            consumer.get();
            reportSuccess(eventName, strategyName);
            recordTimer(eventName, strategyName, OUTCOME_SUCCESS, start);

        } catch (Throwable ex) {
            reportFailure(eventName, strategyName, ex);
            recordTimer(eventName, strategyName, OUTCOME_FAILURE, start);
            throw ex;
        }
    }

    private void recordTimer(String eventName, String strategyName, String outcome, long startTime) {
        String key = strategyName + ":" + outcome;

        timerMap
                .computeIfAbsent(key, k ->
                        Timer.builder(METRIC_PREFIX + ".duration")
                                .description("Event dispatch duration")
                                .tag(STRATEGY_TAG, strategyName)
                                .tag(EVENT_NAME_TAG, eventName)
                                .tag(OUTCOME_TAG, outcome)
                                .publishPercentileHistogram()
                                .register(meterRegistry)
                )
                .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }
}