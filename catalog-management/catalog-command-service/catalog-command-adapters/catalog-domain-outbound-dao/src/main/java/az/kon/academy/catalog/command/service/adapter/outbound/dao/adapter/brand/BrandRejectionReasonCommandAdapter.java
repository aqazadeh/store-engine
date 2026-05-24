package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class BrandRejectionReasonCommandAdapter implements BrandRejectionReasonCommandPort {
    @Override
    public BrandRejectionReasonRoot save(BrandRejectionReasonRoot aggregate) {
        return null;
    }
}
