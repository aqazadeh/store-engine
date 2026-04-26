package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder(toBuilder = true)
public class ProductSpecificationRoot extends AggregateRoot<ProductSpecificationRoot, ProductSpecificationId> {
    @Getter private SpecificationName name;
    @Getter private SpecificationDescription description;
    @Getter private Set<SpecificationCategoryAssignment> categories;

    public static ProductSpecificationRoot initialize(SpecificationCreateCommand command) {
        return ProductSpecificationRoot.builder()
                .id(ProductSpecificationId.random())
                .name(command.getName())
                .description(command.getDescription())
                .categories(new HashSet<>())
                .creationTs(SeDateTime.now())
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing specification information
    }

    public ProductSpecificationRoot assignCategory(SpecificationCategoryAssignment assignments) {
        Set<SpecificationCategoryAssignment> changed = new HashSet<>(this.categories);
        changed.removeIf(a -> assignments.getCategoryId().equals(a.getCategoryId()));
        changed.add(assignments);
        return this.toBuilder()
                .categories(Collections.unmodifiableSet(changed))
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing specification information
    }

    public ProductSpecificationRoot removeCategoryAssignment(SpecificationCategoryAssignment assignments) {
        Set<SpecificationCategoryAssignment> changed = new HashSet<>(this.categories);
        changed.removeIf(a -> assignments.getCategoryId().equals(a.getCategoryId()));
        return this.toBuilder()
                .categories(Collections.unmodifiableSet(changed))
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing specification information
    }

    public ProductSpecificationRoot changeInformation(SpecificationChangeInformationCommand command) {
        return this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for changing specification information
    }
}
