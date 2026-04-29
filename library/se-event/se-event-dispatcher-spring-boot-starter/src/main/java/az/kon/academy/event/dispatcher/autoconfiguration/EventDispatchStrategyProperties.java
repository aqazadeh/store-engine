package az.kon.academy.event.dispatcher.autoconfiguration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties("store-engine.event.dispatcher")
public class EventDispatchStrategyProperties {
    private Map<Class<?>, String> strategies;
    private List<EventDispatchEntry> strategiesEntries;

    @Getter
    @Setter
    static class EventDispatchEntry {
        private String name;
        private Class<?> event;
        private Class<?> strategy;
    }
}
