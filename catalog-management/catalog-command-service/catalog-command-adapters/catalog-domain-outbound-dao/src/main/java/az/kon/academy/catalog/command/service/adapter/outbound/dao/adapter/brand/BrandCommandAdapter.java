package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class BrandCommandAdapter implements BrandCommandPort {
    @Override
    public BrandRoot save(BrandRoot aggregate) {
        return null;
    }
}
