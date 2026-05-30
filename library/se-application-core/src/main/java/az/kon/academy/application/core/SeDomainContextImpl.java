package az.kon.academy.application.core;

import az.kon.academy.domain.core.BaseCommandPort;
import az.kon.academy.domain.core.BaseQueryPort;
import az.kon.academy.domain.core.SeDomainContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeDomainContextImpl implements SeDomainContext {


    private final ApplicationContext applicationContext;

    public SeDomainContextImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T extends BaseQueryPort> T getQueryPort(Class<T> portClass) {
        return this.applicationContext.getBean(portClass);
    }

    @Override
    public <T extends BaseCommandPort> T getCommandPort(Class<T> portClass) {
        return this.applicationContext.getBean(portClass);
    }
}
