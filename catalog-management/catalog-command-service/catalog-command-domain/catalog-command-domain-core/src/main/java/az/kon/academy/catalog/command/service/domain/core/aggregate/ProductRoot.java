package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductRoot extends AggregateRoot<ProductRoot, ProductId> {
    @Getter private final MerchantId merchantId;
    @Getter private ProductCategoryId categoryId;
    @Getter private BrandId brandId;
    @Getter private ProductName name;
    @Getter private ProductDescription description;

    @Getter private final Barcode barcode;
    @Getter private Boolean autoPriceUpdateEnabled;
    @Getter private ProductStatus productStatus;

    @Getter private List<ProductSpecificationAssignment> specifications;
    @Getter private List<ProductVariantRoot> variants;
}
