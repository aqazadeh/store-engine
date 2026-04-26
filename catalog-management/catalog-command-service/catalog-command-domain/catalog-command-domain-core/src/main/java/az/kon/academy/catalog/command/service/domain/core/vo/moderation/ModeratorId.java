package az.kon.academy.catalog.command.service.domain.core.vo.moderation;

import az.kon.academy.aggragate.AggregateId;

import java.util.UUID;

public final class ModeratorId extends AggregateId<UUID> {

    private ModeratorId(UUID value) {
        super(value);
    }

    public static ModeratorId from(UUID value) {
        return new ModeratorId(value);
    }

    public static ModeratorId random() {
        return new ModeratorId(UUID.randomUUID());
    }
}
