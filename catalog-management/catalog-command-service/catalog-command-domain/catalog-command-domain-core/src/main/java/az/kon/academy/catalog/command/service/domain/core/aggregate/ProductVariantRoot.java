package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.NoAggregateRoot;
import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductVariantRoot extends NoAggregateRoot<ProductVariantRoot, ProductVariantId> {
    @Getter private Money minPrice;
    @Getter private Money maxPrice;
    @Getter private Boolean inStock;
    @Getter private List<ProductVariantAssignment> assignments;
}
