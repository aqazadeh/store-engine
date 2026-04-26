package az.kon.academy.catalog.command.service.domain.core.vo.management;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class ProductRejectionReasonId extends AggregateId<UUID> {

    private ProductRejectionReasonId(UUID value) {
        super(value);
    }

    public static ProductRejectionReasonId from(UUID value) {
        return new ProductRejectionReasonId(value);
    }

    public static ProductRejectionReasonId random() {
        return new ProductRejectionReasonId(UUID.randomUUID());
    }
}
