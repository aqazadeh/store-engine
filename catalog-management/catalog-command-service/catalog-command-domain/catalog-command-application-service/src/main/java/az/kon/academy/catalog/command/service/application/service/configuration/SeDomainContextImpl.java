package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.domain.core.SeDomainContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeDomainContextImpl implements SeDomainContext {
    @Override
    public <T> T getQueryPort(Class<T> portClass) {
        return null;
    }

    @Override
    public <T> T getCommandPort(Class<T> portClass) {
        return null;
    }
}
