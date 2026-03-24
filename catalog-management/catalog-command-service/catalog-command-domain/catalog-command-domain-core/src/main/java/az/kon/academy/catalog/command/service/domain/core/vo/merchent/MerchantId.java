package az.kon.academy.catalog.command.service.domain.core.vo.merchent;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class MerchantId extends AggregateId<UUID> {

    private MerchantId(UUID value) {
        super(value);
    }

    public static MerchantId from(UUID value) {
        return new MerchantId(value);
    }

    public static MerchantId random() {
        return new MerchantId(UUID.randomUUID());
    }
}
