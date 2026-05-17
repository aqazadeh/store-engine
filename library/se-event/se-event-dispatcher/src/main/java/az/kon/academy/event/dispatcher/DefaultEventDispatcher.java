package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultEventDispatcher implements EventDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventDispatcher.class);

    private final EventDispatchStrategyRegistry dispatchStrategyRegistry;
    private final EventDispatchMonitor eventDispatchMonitor;

    public DefaultEventDispatcher(EventDispatchStrategyRegistry dispatchStrategyRegistry,
                                  EventDispatchMonitor eventDispatchMonitor) {
        this.dispatchStrategyRegistry = dispatchStrategyRegistry;
        this.eventDispatchMonitor = eventDispatchMonitor;
    }

    @Override
    public <T extends AbstractEvent> void dispatch(List<T> events, UUID correlationId, UUID causationId) {

        logger.info("Start batch | correlationId={} causationId={} size={}",
                correlationId, causationId, events == null ? 0 : events.size());

        if (Objects.isNull(events) || events.isEmpty()) {
            logger.debug("Empty event list received | correlationId={}", correlationId);
            return;
        }

        Map<EventDispatchStrategy, List<EventMessage<T>>> strategyGroups = new HashMap<>();

        for (T event : events) {
            String eventType = event.getClass().getSimpleName();
            logger.debug("Event received | type={} eventId={} correlationId={} causationId={}",
                    eventType, event.getEventId(), correlationId, causationId);

            eventDispatchMonitor.reportDispatched(eventType);

            EventDispatchStrategy strategy =
                    dispatchStrategyRegistry.get(event.getClass())
                            .orElse(EventDispatchStrategy.NOOP);

            if (strategy == EventDispatchStrategy.NOOP) {
                logger.warn("No strategy found | eventType={} eventId={}", eventType, event.getEventId());
                eventDispatchMonitor.reportDispatcherNotFound(eventType);
                continue;
            }

            String triggerBy = "user"; // FIXME: EventContext
            var eventMessage = EventMessage.of(event, correlationId, causationId, triggerBy);
            logger.debug("EventMessage created | type={} messageId={} strategy={}",
                    eventType, eventMessage.getId(), strategy.getClass().getSimpleName());
            strategyGroups.computeIfAbsent(strategy, k -> new ArrayList<>())
                    .add(eventMessage);
        }

        logger.info("Strategy grouping done | groups={}", strategyGroups.size());
        for (Map.Entry<EventDispatchStrategy, List<EventMessage<T>>> entry : strategyGroups.entrySet()) {
            EventDispatchStrategy strategy = entry.getKey();
            List<EventMessage<T>> messages = entry.getValue();

            String strategyName = strategy.getClass().getSimpleName();
            String eventType = messages.getFirst().getPayload().getClass().getSimpleName();

            logger.info("Execute strategy | strategy={} eventType={} batchSize={}",
                    strategyName, eventType, messages.size());
            
            try {
                eventDispatchMonitor.record(eventType, strategyName, () -> {
                    strategy.proceed(messages);
                    return null;
                });
                logger.info("Strategy completed | strategy={} eventType={}", strategyName, eventType);
            } catch (Exception ex) {
                logger.error("Strategy failed | strategy={} eventType={}", strategyName, eventType, ex);
                throw ex;
            }
        }
        logger.info("Batch completed | correlationId={}", correlationId);
    }
}