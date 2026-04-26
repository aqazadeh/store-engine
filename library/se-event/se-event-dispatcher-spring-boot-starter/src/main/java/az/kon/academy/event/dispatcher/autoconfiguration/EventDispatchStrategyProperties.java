package az.kon.academy.event.dispatcher.autoconfiguration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties("store-engine.event.dispatcher")
public class EventDispatchStrategyProperties {
    private Map<Class<?>, String> strategies;
}
