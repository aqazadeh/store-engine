package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductRejectionReasonCommandAdapter implements ProductRejectionReasonCommandPort {

    @Override
    public ProductRejectionReasonRoot save(ProductRejectionReasonRoot aggregate) {
        return null;
    }
}
