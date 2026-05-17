package az.kon.academy.event.handler.autoconfiguration.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("store-engine.event.handler")
public class EventHandlerConfigurationProperties {
    private EventHandlerMetricConfigData metrics;
    private EventHandlerLoggingConfigData logging;
}
