package az.kon.academy.catalog.command.service.domain.core.vo.attribute;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class ProductAttributeId extends AggregateId<UUID> {

    private ProductAttributeId(UUID value) {
        super(value);
    }

    public static ProductAttributeId from(UUID value) {
        return new ProductAttributeId(value);
    }

    public static ProductAttributeId random() {
        return new ProductAttributeId(UUID.randomUUID());
    }
}
