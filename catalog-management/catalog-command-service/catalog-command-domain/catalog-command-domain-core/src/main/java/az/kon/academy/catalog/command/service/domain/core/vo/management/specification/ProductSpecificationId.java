package az.kon.academy.catalog.command.service.domain.core.vo.management.specification;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class ProductSpecificationId extends AggregateId<UUID> {

    private ProductSpecificationId(UUID value) {
        super(value);
    }

    public static ProductSpecificationId from(UUID value) {
        return new ProductSpecificationId(value);
    }

    public static ProductSpecificationId random() {
        return new ProductSpecificationId(UUID.randomUUID());
    }
}
