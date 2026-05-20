package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.catalog.event.management.specification.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuperBuilder(toBuilder = true)
public class ProductSpecificationRoot extends AggregateRoot<ProductSpecificationRoot, ProductSpecificationId> {
    @Getter private final SpecificationName name;
    @Getter private final SpecificationDescription description;
    @Getter private final Set<SpecificationCategoryAssignment> categories;

    public static ProductSpecificationRoot initialize(SpecificationCreateCommand command) {
        var specification = ProductSpecificationRoot.builder()
                .id(ProductSpecificationId.random())
                .name(command.getName())
                .description(command.getDescription())
                .categories(new HashSet<>())
                .creationTs(SeDateTime.now())
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductSpecificationCreatedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime(),
                specification.getName().value(),
                specification.getDescription().value()
        );

        specification.addEvent(event);
        return specification;
    }

    public ProductSpecificationRoot assignCategory(SpecificationCategoryAssignment assignment) {
        Set<SpecificationCategoryAssignment> changed = new HashSet<>(this.categories);
        changed.removeIf(a -> assignment.getCategoryId().equals(a.getCategoryId()));
        changed.add(assignment);

        var specification = this.toBuilder()
                .categories(Collections.unmodifiableSet(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductSpecificationCategoryAssignedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime(),
                assignment.getCategoryId().value(),
                assignment.isRequired()
        );

        specification.addEvent(event);
        return specification;
    }

    public ProductSpecificationRoot removeCategoryAssignment(SpecificationCategoryAssignment assignment) {
        Set<SpecificationCategoryAssignment> changed = new HashSet<>(this.categories);
        changed.removeIf(a -> assignment.getCategoryId().equals(a.getCategoryId()));

        var specification = this.toBuilder()
                .categories(Collections.unmodifiableSet(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductSpecificationCategoryRemovedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime(),
                assignment.getCategoryId().value()
        );

        specification.addEvent(event);
        return specification;
    }

    public ProductSpecificationRoot changeInformation(SpecificationChangeInformationCommand command) {
        var specification = this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductSpecificationInformationChangedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime(),
                specification.getName().value(),
                specification.getDescription().value()
        );

        specification.addEvent(event);
        return specification;
    }
}
