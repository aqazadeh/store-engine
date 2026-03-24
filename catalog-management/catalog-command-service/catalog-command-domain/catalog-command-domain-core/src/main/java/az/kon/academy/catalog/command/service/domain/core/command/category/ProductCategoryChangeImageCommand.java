package az.kon.academy.catalog.command.service.domain.core.command.category;

import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ProductCategoryChangeImageCommand {

    @Getter
    private final ProductCategoryId productCategoryId;

    @Getter
    private final String image; //Fixme when file storage is implemented, change it to Image value object
}
