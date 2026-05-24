package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductSpecificationCommandAdapter implements ProductSpecificationCommandPort {

    @Override
    public ProductSpecificationRoot save(ProductSpecificationRoot aggregate) {
        return null;
    }
}
