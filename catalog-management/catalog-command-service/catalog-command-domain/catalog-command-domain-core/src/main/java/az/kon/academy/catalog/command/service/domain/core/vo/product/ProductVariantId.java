package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class ProductVariantId extends AggregateId<UUID> {

    private ProductVariantId(UUID value) {
        super(value);
    }

    public static ProductVariantId from(UUID value) {
        return new ProductVariantId(value);
    }

    public static ProductVariantId random() {
        return new ProductVariantId(UUID.randomUUID());
    }
}
