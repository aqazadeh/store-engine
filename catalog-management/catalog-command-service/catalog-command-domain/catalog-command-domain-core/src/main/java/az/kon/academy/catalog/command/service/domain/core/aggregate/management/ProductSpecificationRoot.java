package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationCategoryAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.catalog.event.management.specification.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuperBuilder(toBuilder = true)
public class ProductSpecificationRoot extends AggregateRoot<ProductSpecificationRoot, ProductSpecificationId> {
    @Getter private final SpecificationName name;
    @Getter private final SpecificationDescription description;
    @Getter private final Set<ProductSpecificationCategoryAssignment> categories;

    public static ProductSpecificationRoot initialize(ProductSpecificationCreateCommand command) {
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

    public ProductSpecificationRoot assignCategory(ProductSpecificationAssignCategoryCommand command) {

        if(this.findAssignment(command.getCategoryId()).isEmpty()) return this;

        var changed = new HashSet<>(this.categories);
        var assignment = ProductSpecificationCategoryAssignment.initialize(
                command.getCategoryId(),
                command.isRequired()
        );
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

    public ProductSpecificationRoot removeCategoryAssignment(ProductSpecificationRemoveCategoryAssignmentCommand command) {
        var assignment = this.findAssignment(command.getCategoryId());
        if(assignment.isEmpty()) return this;

        var changed = new HashSet<>(this.categories);
        changed.remove(assignment.get());

        var specification = this.toBuilder()
                .categories(Collections.unmodifiableSet(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductSpecificationCategoryRemovedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime(),
                command.getCategoryId().value()
        );

        specification.addEvent(event);
        return specification;
    }

    public ProductSpecificationRoot changeInformation(ProductSpecificationChangeInformationCommand command) {
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

    public ProductSpecificationRoot delete() {
        var specification = this.markAsDeleted();

        var event = ProductSpecificationDeletedEvent.of(
                specification.getRootID().value().toString(),
                specification.getModificationTs().toOffsetDateTime()
        );

        specification.addEvent(event);
        return specification;
    }

    private Optional<ProductSpecificationCategoryAssignment> findAssignment(ProductCategoryId productCategoryId) {
        return this.categories.stream()
                .filter(assignment -> assignment.getCategoryId().equals(productCategoryId))
                .findFirst();
    }
}
