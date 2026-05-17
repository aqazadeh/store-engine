package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductName;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductChangeInformationCommand {
    @Getter private final ProductId productId;
    @Getter private final ProductName name;
    @Getter private final ProductDescription description;
}