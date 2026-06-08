package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductSpecificationValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public final class ProductAssignSpecificationsCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductId productId;
    @Getter private final List<SpecificationEntry> entries;

    @Builder
    public record SpecificationEntry(ProductSpecificationId specificationId, ProductSpecificationValue value) {
    }
}
