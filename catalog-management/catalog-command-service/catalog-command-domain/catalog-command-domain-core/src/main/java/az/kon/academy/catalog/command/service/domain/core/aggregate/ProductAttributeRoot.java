package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.attribute.ProductAttributeChangeCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.attribute.ProductAttributeChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.attribute.ProductAttributeCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeId;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeName;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ProductAttributeRoot extends AggregateRoot<ProductAttributeRoot, ProductAttributeId> {

    @Getter private ProductCategoryId categoryId;
    @Getter private ProductAttributeName name;
    @Getter private ProductAttributeDescription description;
    @Getter private Boolean isRequired;

    public static ProductAttributeRoot initialize(ProductAttributeCreateCommand command) {
        return ProductAttributeRoot.builder()
                .id(ProductAttributeId.random())
                .categoryId(command.getCategoryId())
                .name(command.getName())
                .description(command.getDescription())
                .isRequired(command.getIsRequired())
                .creationTs(SeDateTime.now())
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for creating attribute
    }

    public ProductAttributeRoot markAsRequired() {
        return this.toBuilder()
                .isRequired(Boolean.TRUE)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for marking attribute as required
    }

    public ProductAttributeRoot markAsNonRequired() {
        return this.toBuilder()
                .isRequired(Boolean.FALSE)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for marking attribute as non-required
    }

    public ProductAttributeRoot changeInformation(ProductAttributeChangeInformationCommand command) {
        return this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing attribute information
    }

    public ProductAttributeRoot changeCategory(ProductAttributeChangeCategoryCommand command) {
        return this.toBuilder()
                .categoryId(command.getCategoryId())
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing attribute category
    }
}
