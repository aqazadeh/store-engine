package az.kon.academy.catalog.command.service.domain.core.vo.management.variant;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public class VariantKeyId extends AggregateId<UUID> {

    private VariantKeyId(UUID value) {
        super(value);
    }

    public static VariantKeyId from(UUID value) {
        return new VariantKeyId(value);
    }

    public static VariantKeyId random() {
        return new VariantKeyId(UUID.randomUUID());
    }
}
