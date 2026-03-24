package az.kon.academy.catalog.command.service.domain.core.vo.brand;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class BrandId extends AggregateId<UUID> {

    private BrandId(UUID value) {
        super(value);
    }

    public static BrandId from(UUID value) {
        return new BrandId(value);
    }

    public static BrandId random() {
        return new BrandId(UUID.randomUUID());
    }
}
