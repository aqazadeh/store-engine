package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductStockCommandAdapter implements ProductStockCommandPort {
    @Override
    public ProductStockRoot save(ProductStockRoot aggregate) {
        return null;
    }
}
