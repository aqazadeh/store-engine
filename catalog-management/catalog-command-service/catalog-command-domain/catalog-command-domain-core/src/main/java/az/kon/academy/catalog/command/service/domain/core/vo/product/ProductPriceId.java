package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class ProductPriceId extends AggregateId<UUID> {

    private ProductPriceId(UUID value) {
        super(value);
    }

    public static ProductPriceId from(UUID value) {
        return new ProductPriceId(value);
    }

    public static ProductPriceId random() {
        return new ProductPriceId(UUID.randomUUID());
    }
}