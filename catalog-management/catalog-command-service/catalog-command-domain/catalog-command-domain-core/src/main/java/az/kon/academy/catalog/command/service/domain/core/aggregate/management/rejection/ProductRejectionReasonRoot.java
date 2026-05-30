package az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.event.productrejection.ProductRejectionReasonCreatedEvent;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductRejectionReasonRoot extends AggregateRoot<ProductRejectionReasonRoot, ProductRejectionReasonId> {
    @Getter private final ProductId productId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;

    public static ProductRejectionReasonRoot initialize(ProductCreateRejectionReasonCommand command) {
        var rejectionReason = ProductRejectionReasonRoot.builder()
                .id(ProductRejectionReasonId.random())
                .productId(command.getProductId())
                .reason(command.getReason())
                .moderatedBy(command.getModeratedBy())
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
}
