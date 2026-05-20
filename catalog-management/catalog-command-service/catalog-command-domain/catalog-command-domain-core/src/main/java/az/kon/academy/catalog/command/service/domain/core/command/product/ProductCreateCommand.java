package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductName;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductCreateCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductCategoryId categoryId;
    @Getter private final BrandId brandId;
    @Getter private final ProductName name;
    @Getter private final ProductDescription description;
    @Getter private final Barcode barcode;
}