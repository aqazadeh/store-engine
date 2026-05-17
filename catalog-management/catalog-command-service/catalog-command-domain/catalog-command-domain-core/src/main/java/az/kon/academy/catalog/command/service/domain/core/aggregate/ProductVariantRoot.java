package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.NoAggregateRoot;
import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAddVariantCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductVariantRoot extends NoAggregateRoot<ProductVariantRoot, ProductVariantId> {

    @Getter private Money minPrice;
    @Getter private Money maxPrice;
    @Getter private Boolean inStock;
    @Getter private List<ProductVariantAssignment> assignments;

    public static ProductVariantRoot initialize(ProductAddVariantCommand command) {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .assignments(List.copyOf(command.getAssignments()))
                .minPrice(command.getMinPrice())
                .maxPrice(command.getMaxPrice())
                .inStock(command.getInStock())
                .build();
    }

    public ProductVariantRoot updatePrice(Money newMinPrice, Money newMaxPrice) {
        return this.toBuilder()
                .minPrice(newMinPrice)
                .maxPrice(newMaxPrice)
                .build();
    }

    public ProductVariantRoot setInStock(Boolean inStock) {
        return this.toBuilder()
                .inStock(inStock)
                .build();
    }

    public ProductVariantRoot addAssignment(ProductVariantAssignment assignment) {
        var changed = new ArrayList<>(this.assignments);
        changed.removeIf(a -> assignment.getVariantKeyId().equals(a.getVariantKeyId()));
        changed.add(assignment);
        return this.toBuilder()
                .assignments(Collections.unmodifiableList(changed))
                .build();
    }

    public ProductVariantRoot removeAssignment(VariantKeyId variantKeyId) {
        var changed = this.assignments.stream()
                .filter(a -> !variantKeyId.value().equals(a.getVariantKeyId().value()))
                .toList();
        return this.toBuilder()
                .assignments(Collections.unmodifiableList(changed))
                .build();
    }
}