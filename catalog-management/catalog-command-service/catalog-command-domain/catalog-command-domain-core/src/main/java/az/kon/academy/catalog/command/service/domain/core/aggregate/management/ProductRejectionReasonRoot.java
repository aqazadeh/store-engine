package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductRejectionReasonRoot extends AggregateRoot<ProductRejectionReasonRoot, ProductRejectionReasonId> {
    @Getter private final ProductId productId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;

    public ProductRejectionReasonRoot initialize() {
        return null;
    }
}
