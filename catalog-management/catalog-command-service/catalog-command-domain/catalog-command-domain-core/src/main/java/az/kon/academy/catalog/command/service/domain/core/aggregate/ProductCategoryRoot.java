package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeParentCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryPath;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductCategoryRoot extends AggregateRoot<ProductCategoryRoot, ProductCategoryId> {

    @Getter private ProductCategoryId parent;
    @Getter private ProductCategoryName name;
    @Getter private ProductCategoryPath path;
    @Getter private ProductCategoryDescription description;
    @Getter private String image; //Fixme when file storage is implemented, change it to Image value object

    public static ProductCategoryRoot initialize(ProductCategoryCreateCommand command) {
        return ProductCategoryRoot.builder()
                .id(ProductCategoryId.random())
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .build();
        //Fixme when implement event system, add event for category creation
    }

    public final ProductCategoryRoot changeImage(ProductCategoryChangeImageCommand command) {
        return this.toBuilder()
                .image(command.getImage())
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for image change
    }

    public final ProductCategoryRoot changeInformation(ProductCategoryChangeInformationCommand command) {
        return this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for category information change
    }

    public ProductCategoryRoot changeParent(ProductCategoryChangeParentCommand command) {
        return this.toBuilder()
                .parent(command.getParentId())
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for category parent change
    }

    public ProductCategoryRoot removeParent() {
        return this.toBuilder()
                .parent(null)
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for category parent removal
    }
}