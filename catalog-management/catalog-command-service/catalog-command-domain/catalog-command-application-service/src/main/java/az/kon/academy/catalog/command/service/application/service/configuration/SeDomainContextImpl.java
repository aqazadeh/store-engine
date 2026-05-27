package az.kon.academy.catalog.command.service.application.service.configuration;

import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;
import az.kon.academy.domain.core.BaseCommandPort;
import az.kon.academy.domain.core.BaseQueryPort;
import az.kon.academy.domain.core.SeDomainContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
