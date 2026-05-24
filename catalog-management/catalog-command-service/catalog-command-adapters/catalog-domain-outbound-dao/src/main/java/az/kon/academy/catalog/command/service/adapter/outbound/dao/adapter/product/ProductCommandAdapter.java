package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductCommandAdapter implements ProductCommandPort {
    @Override
    public ProductRoot save(ProductRoot aggregate) {
        return null;
    }
}
