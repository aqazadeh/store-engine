package az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonChangedReasonEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonCreatedEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonDeletedEvent;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonSolvedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductRejectionReasonRoot extends AggregateRoot<ProductRejectionReasonRoot, ProductRejectionReasonId> {
    @Getter private final ProductId productId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;
    @Getter private final Boolean solved;

    public static ProductRejectionReasonRoot initialize(ProductCreateRejectionReasonCommand command) {
        var rejectionReason = ProductRejectionReasonRoot.builder()
                .id(ProductRejectionReasonId.random())
                .productId(command.getProductId())
                .reason(command.getReason())
                .moderatedBy(command.getModeratedBy())
                .solved(Boolean.FALSE)
                .build();

        var event = ProductRejectionReasonCreatedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getProductId().value(),
                rejectionReason.getReason(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public ProductRejectionReasonRoot markAsSolved() {
        if (this.solved == Boolean.TRUE) return this;

        var rejectionReason = this.toBuilder()
                .solved(Boolean.TRUE)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductRejectionReasonSolvedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.solved
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public ProductRejectionReasonRoot changeReason(ProductRejectionReasonChangeReasonCommand command) {

        if (this.reason.equals(command.getReason())) return this;

        var rejectionReason = this.toBuilder()
                .reason(command.getReason())
                .moderatedBy(command.getModeratorId())
                .build();

        var event = ProductRejectionReasonChangedReasonEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getReason(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }

    public ProductRejectionReasonRoot remove(ProductRejectionReasonRemoveCommand command) {
        var rejectionReason = super.markAsDeleted()
                .toBuilder()
                .moderatedBy(command.getModeratorId())
                .build();

        var event = ProductRejectionReasonDeletedEvent.of(
                rejectionReason.getRootID().value().toString(),
                rejectionReason.getModificationTs().toOffsetDateTime(),
                rejectionReason.getModeratedBy().value()
        );
        rejectionReason.addEvent(event);
        return rejectionReason;
    }
}
