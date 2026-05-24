package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductCategoryCommandAdapter implements ProductCategoryCommandPort {
    @Override
    public ProductCategoryRoot save(ProductCategoryRoot aggregate) {
        return null;
    }
}
