package az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.event.management.rejection.BrandRejectionReasonCreatedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class BrandRejectionReasonRoot extends AggregateRoot<BrandRejectionReasonRoot, BrandRejectionReasonId> {
    @Getter private final BrandId brandId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;

    public static BrandRejectionReasonRoot initialize(BrandCreateRejectionReasonCommand command) {
        var rejectionReason = BrandRejectionReasonRoot.builder()
                .id(BrandRejectionReasonId.random())
                .brandId(command.getBrandId())
                .reason(command.getReason())
                .moderatedBy(command.getModeratedBy())
                .build();

        var event = BrandRejectionReasonCreatedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getBrandId().value(),
                rejectionReason.getReason(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }
}
