package az.kon.academy.catalog.command.service.domain.core.vo.management.variant;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class VariantValueId extends AggregateId<UUID> {

    private VariantValueId(UUID value) {
        super(value);
    }

    public static VariantValueId from(UUID value) {
        return new VariantValueId(value);
    }

    public static VariantValueId random() {
        return new VariantValueId(UUID.randomUUID());
    }
}
