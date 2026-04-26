package az.kon.academy.catalog.command.service.domain.core.vo.management.category;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class ProductCategoryId extends AggregateId<UUID> {

    private ProductCategoryId(UUID value) {
        super(value);
    }

    public static ProductCategoryId from(UUID value) {
        return new ProductCategoryId(value);
    }

    public static ProductCategoryId random() {
        return new ProductCategoryId(UUID.randomUUID());
    }
}
