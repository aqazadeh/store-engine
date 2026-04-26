package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class ProductId extends AggregateId<UUID> {

    private ProductId(UUID value) {
        super(value);
    }

    public static ProductId from(UUID value) {
        return new ProductId(value);
    }

    public static ProductId random() {
        return new ProductId(UUID.randomUUID());
    }
}
