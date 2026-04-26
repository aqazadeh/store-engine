package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import lombok.Getter;

@Getter
public final class ProductSpecificationAssignment {
    private final ProductSpecificationId specificationId;
    private final ProductSpecificationValue value;

    private ProductSpecificationAssignment(ProductSpecificationId specificationId, ProductSpecificationValue value) {
        this.specificationId = specificationId;
        this.value = value;
    }

    public static ProductSpecificationAssignment of(ProductSpecificationId specificationId, ProductSpecificationValue value) {
        return new ProductSpecificationAssignment(specificationId, value);
    }
}
