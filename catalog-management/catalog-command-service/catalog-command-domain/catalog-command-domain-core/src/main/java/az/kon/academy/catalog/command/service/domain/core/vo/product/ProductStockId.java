package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class ProductStockId extends AggregateId<UUID> {

    private ProductStockId(UUID value) {
        super(value);
    }

    public static ProductStockId from(UUID value) {
        return new ProductStockId(value);
    }

    public static ProductStockId random() {
        return new ProductStockId(UUID.randomUUID());
    }
}