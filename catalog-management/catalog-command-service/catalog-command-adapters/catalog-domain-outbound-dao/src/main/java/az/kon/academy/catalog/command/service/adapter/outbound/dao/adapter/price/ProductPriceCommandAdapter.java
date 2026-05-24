package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductPriceCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import org.springframework.stereotype.Component;

@Component //FIXME change to custom annotation. @CommandAdapter
public class ProductPriceCommandAdapter implements ProductPriceCommandPort {
    @Override
    public ProductPriceRoot save(ProductPriceRoot aggregate) {
        return null;
    }
}
