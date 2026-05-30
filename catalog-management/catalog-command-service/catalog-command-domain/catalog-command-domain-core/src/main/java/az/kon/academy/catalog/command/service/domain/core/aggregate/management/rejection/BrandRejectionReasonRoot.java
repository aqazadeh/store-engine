package az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonAddedEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonChangedReasonEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonDeletedEvent;
import az.kon.academy.catalog.event.brandrejection.BrandRejectionReasonSolvedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class BrandRejectionReasonRoot extends AggregateRoot<BrandRejectionReasonRoot, BrandRejectionReasonId> {
    @Getter private final BrandId brandId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;
    @Getter private final Boolean solved;

    public static BrandRejectionReasonRoot initialize(BrandRejectionReasonAddCommand command) {
        var rejectionReason = BrandRejectionReasonRoot.builder()
                .id(BrandRejectionReasonId.random())
                .brandId(command.getBrandId())
                .reason(command.getReason())
                .solved(Boolean.FALSE)
                .moderatedBy(command.getModeratorId())
                .build();

        var event = BrandRejectionReasonAddedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getBrandId().value(),
                rejectionReason.getReason(),
                rejectionReason.getModeratedBy().value(),
                rejectionReason.solved
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public BrandRejectionReasonRoot markAsSolved() {
        if (this.solved == Boolean.TRUE) return this;

        var rejectionReason =  this.toBuilder()
                .solved(Boolean.TRUE)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandRejectionReasonSolvedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.solved
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public BrandRejectionReasonRoot changeReason(BrandRejectionReasonChangeReasonCommand command) {

        if( this.reason.equals(command.getReason())) return this;

        var rejectionReason =  this.toBuilder()
                .reason(command.getReason())
                .moderatedBy(command.getModeratorId())
                .build();
        var event = BrandRejectionReasonChangedReasonEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getReason(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public BrandRejectionReasonRoot remove(BrandRejectionReasonRemoveCommand command) {
        var rejectionReason = super.markAsDeleted()
                .toBuilder()
                .moderatedBy(command.getModeratorId())
                .build();

        var event = BrandRejectionReasonDeletedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }
}
