package az.kon.academy.event.dispatcher.autoconfiguration.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties("store-engine.event.dispatcher")
public class EventDispatchStrategyProperties {
    private List<EventDispatchEntry> strategies;
}
