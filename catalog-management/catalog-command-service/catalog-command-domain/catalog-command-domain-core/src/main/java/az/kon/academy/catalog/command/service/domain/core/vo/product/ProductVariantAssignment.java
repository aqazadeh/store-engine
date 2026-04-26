package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import lombok.Getter;

@Getter
public final class ProductVariantAssignment {
    private final VariantKeyId variantKeyId;
    private final VariantValueId variantValueId;

    private ProductVariantAssignment(VariantKeyId variantKeyId, VariantValueId variantValueId) {
        this.variantKeyId = variantKeyId;
        this.variantValueId = variantValueId;
    }

    public static ProductVariantAssignment of(VariantKeyId variantKeyId, VariantValueId variantValueId) {
        return new ProductVariantAssignment(variantKeyId, variantValueId);
    }
}
