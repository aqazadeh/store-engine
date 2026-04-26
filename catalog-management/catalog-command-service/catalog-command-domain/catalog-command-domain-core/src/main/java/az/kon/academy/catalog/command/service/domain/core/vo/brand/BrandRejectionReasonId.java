package az.kon.academy.catalog.command.service.domain.core.vo.brand;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class BrandRejectionReasonId extends AggregateId<UUID> {

    private BrandRejectionReasonId(UUID value) {
        super(value);
    }

    public static BrandRejectionReasonId from(UUID value) {
        return new BrandRejectionReasonId(value);
    }

    public static BrandRejectionReasonId random() {
        return new BrandRejectionReasonId(UUID.randomUUID());
    }
}
