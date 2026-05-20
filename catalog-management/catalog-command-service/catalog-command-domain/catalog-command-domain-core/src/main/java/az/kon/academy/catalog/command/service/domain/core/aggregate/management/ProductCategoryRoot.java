package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeParentCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
import az.kon.academy.catalog.event.management.category.*;
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
        var productCategory = ProductCategoryRoot.builder()
                .id(ProductCategoryId.random())
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .build();

        var event = ProductCategoryCreatedEvent.of(
                productCategory.getRootID().value().toString(),
                productCategory.getModificationTs().toOffsetDateTime(),
                productCategory.getName().value(),
                productCategory.getPath().value(),
                productCategory.getDescription().value(),
                productCategory.getImage()
        );

        productCategory.addEvent(event);
        return productCategory;
    }

    public ProductCategoryRoot changeImage(ProductCategoryChangeImageCommand command) {
        var productCategory = this.toBuilder()
                .image(command.getImage())
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductCategoryImageChangedEvent.of(
                productCategory.getRootID().value().toString(),
                productCategory.getModificationTs().toOffsetDateTime(),
                productCategory.getImage()
        );

        productCategory.addEvent(event);
        return productCategory;
    }

    public ProductCategoryRoot changeInformation(ProductCategoryChangeInformationCommand command) {
        var productCategory = this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductCategoryInformationChangedEvent.of(
                productCategory.getRootID().value().toString(),
                productCategory.getModificationTs().toOffsetDateTime(),
                productCategory.getName().value(),
                productCategory.getPath().value(),
                productCategory.getDescription().value()
        );

        productCategory.addEvent(event);
        return productCategory;
    }

    public ProductCategoryRoot changeParent(ProductCategoryId parentId) {
        var productCategory = this.toBuilder()
                .parent(parentId)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductCategoryParentChangedEvent.of(
                productCategory.getRootID().value().toString(),
                productCategory.getModificationTs().toOffsetDateTime(),
                productCategory.getParent().value()
        );
        productCategory.addEvent(event);
        return productCategory;
    }

    public ProductCategoryRoot removeParent() {
        var productCategory = this.toBuilder()
                .parent(null)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductCategoryParentRemovedEvent.of(
                productCategory.getRootID().value().toString(),
                productCategory.getModificationTs().toOffsetDateTime()
        );
        productCategory.addEvent(event);
        return productCategory;
    }
}