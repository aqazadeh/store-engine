package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class BrandRejectionReasonRoot extends AggregateRoot<BrandRejectionReasonRoot, BrandRejectionReasonId> {
    @Getter private final BrandId brandId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;
}
