package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryAssignedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryRemovedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCreatedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationInformationChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductSpecificationRoot")
class ProductSpecificationRootTest {

    private SpecificationName name;
    private SpecificationDescription description;
    private SpecificationCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        name = new SpecificationName("Color Spec");
        description = new SpecificationDescription("Defines the color attribute of a product");
        createCommand = SpecificationCreateCommand.builder()
                .name(name)
                .description(description)
                .build();
    }

    private ProductSpecificationRoot emptySpecification() {
        return ProductSpecificationRoot.initialize(createCommand);
    }

    private ProductSpecificationRoot specificationWithCategory(ProductCategoryId categoryId, boolean required) {
        var assignment = SpecificationCategoryAssignment.initialize(categoryId, required);
        return emptySpecification().assignCategory(assignment);
    }

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getRootID()).isNotNull();
                assertThat(spec.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductSpecificationRoot.initialize(createCommand);
                var second = ProductSpecificationRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getName()).isEqualTo(name);
                assertThat(spec.getName().value()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescriptionFromCommand() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getDescription()).isEqualTo(description);
                assertThat(spec.getDescription().value()).isEqualTo(description.value());
            }

            @Test
            @DisplayName("Initializes with empty categories set")
            void initializesWithEmptyCategories() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getCategories()).isNotNull();
                assertThat(spec.getCategories()).isEmpty();
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationCreatedEvent")
            void registeredEventIsCorrectType() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

                assertThat(spec.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecificationId() {
                var spec = ProductSpecificationRoot.initialize(createCommand);
                var event = spec.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(spec.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var spec = ProductSpecificationRoot.initialize(createCommand);
                var event = spec.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var spec = ProductSpecificationRoot.initialize(createCommand);
                var event = spec.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(spec.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the name from command")
            void eventCarriesName() {
                var spec = ProductSpecificationRoot.initialize(createCommand);
                var event = (ProductSpecificationCreatedEvent) spec.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Event carries the description from command")
            void eventCarriesDescription() {
                var spec = ProductSpecificationRoot.initialize(createCommand);
                var event = (ProductSpecificationCreatedEvent) spec.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(description.value());
            }
        }
    }

    @Nested
    @DisplayName("assignCategory()")
    class AssignCategory {

        private ProductCategoryId categoryId;
        private SpecificationCategoryAssignment assignment;

        @BeforeEach
        void setUpAssignment() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            assignment = SpecificationCategoryAssignment.initialize(categoryId, true);
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds category to the set")
            void addsCategoryToSet() {
                var result = emptySpecification().assignCategory(assignment);

                assertThat(result.getCategories()).hasSize(1);
            }

            @Test
            @DisplayName("Category in set has the correct categoryId")
            void categoryHasCorrectId() {
                var result = emptySpecification().assignCategory(assignment);
                var stored = result.getCategories().iterator().next();

                assertThat(stored.getCategoryId()).isEqualTo(categoryId);
            }

            @Test
            @DisplayName("Category in set has the correct isRequired flag")
            void categoryHasCorrectRequiredFlag() {
                var result = emptySpecification().assignCategory(assignment);
                var stored = result.getCategories().iterator().next();

                assertThat(stored.isRequired()).isTrue();
            }

            @Test
            @DisplayName("Assigning two different categories results in two entries")
            void assigningTwoDifferentCategoriesResultsInTwo() {
                var secondCategoryId = ProductCategoryId.from(UUID.randomUUID());
                var secondAssignment = SpecificationCategoryAssignment.initialize(secondCategoryId, false);

                var result = emptySpecification()
                        .assignCategory(assignment)
                        .assignCategory(secondAssignment);

                assertThat(result.getCategories()).hasSize(2);
            }

            @Test
            @DisplayName("Re-assigning same categoryId replaces the existing entry (upsert)")
            void reassigningSameCategoryIdReplacesExisting() {
                var updatedAssignment = SpecificationCategoryAssignment.initialize(categoryId, false);

                var result = emptySpecification()
                        .assignCategory(assignment)
                        .assignCategory(updatedAssignment);

                assertThat(result.getCategories()).hasSize(1);
                var stored = result.getCategories().iterator().next();
                assertThat(stored.isRequired()).isFalse();
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = emptySpecification().assignCategory(assignment);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate categories are unchanged")
            void originalAggregateIsUnchanged() {
                var original = emptySpecification();
                original.assignCategory(assignment);

                assertThat(original.getCategories()).isEmpty();
            }

            @Test
            @DisplayName("Other fields are preserved after category assignment")
            void otherFieldsPreserved() {
                var original = emptySpecification();
                var result = original.assignCategory(assignment);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = emptySpecification().assignCategory(assignment);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationCategoryAssignedEvent")
            void registeredEventIsCorrectType() {
                var result = emptySpecification().assignCategory(assignment);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationCategoryAssignedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecificationId() {
                var result = emptySpecification().assignCategory(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = emptySpecification().assignCategory(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = emptySpecification().assignCategory(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the categoryId from assignment")
            void eventCarriesCategoryId() {
                var result = emptySpecification().assignCategory(assignment);
                var event = (ProductSpecificationCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getCategoryId()).isEqualTo(categoryId.value());
            }

            @Test
            @DisplayName("Event carries the required flag from assignment")
            void eventCarriesRequiredFlag() {
                var result = emptySpecification().assignCategory(assignment);
                var event = (ProductSpecificationCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.isRequired()).isTrue();
            }

            @Test
            @DisplayName("Event carries required=false when assignment is not required")
            void eventCarriesRequiredFalse() {
                var optionalAssignment = SpecificationCategoryAssignment.initialize(categoryId, false);
                var result = emptySpecification().assignCategory(optionalAssignment);
                var event = (ProductSpecificationCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.isRequired()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("removeCategoryAssignment()")
    class RemoveCategoryAssignment {

        private ProductCategoryId categoryId;
        private SpecificationCategoryAssignment assignment;

        @BeforeEach
        void setUpAssignment() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            assignment = SpecificationCategoryAssignment.initialize(categoryId, true);
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Removes the matching category from the set")
            void removesMatchingCategory() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);

                assertThat(result.getCategories()).isEmpty();
            }

            @Test
            @DisplayName("Removes by categoryId regardless of isRequired flag")
            void removesByCategoryIdRegardlessOfRequiredFlag() {
                var differentFlagAssignment = SpecificationCategoryAssignment.initialize(categoryId, false);

                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(differentFlagAssignment);

                assertThat(result.getCategories()).isEmpty();
            }

            @Test
            @DisplayName("Removing non-existent category leaves categories unchanged")
            void removingNonExistentCategoryLeavesSetUnchanged() {
                var spec = specificationWithCategory(categoryId, true);
                var otherCategoryId = ProductCategoryId.from(UUID.randomUUID());
                var otherAssignment = SpecificationCategoryAssignment.initialize(otherCategoryId, false);

                var result = spec.removeCategoryAssignment(otherAssignment);

                assertThat(result.getCategories()).hasSize(1);
            }

            @Test
            @DisplayName("Removes only the targeted category when multiple exist")
            void removesOnlyTargetedCategory() {
                var secondCategoryId = ProductCategoryId.from(UUID.randomUUID());
                var secondAssignment = SpecificationCategoryAssignment.initialize(secondCategoryId, false);

                var result = emptySpecification()
                        .assignCategory(assignment)
                        .assignCategory(secondAssignment)
                        .removeCategoryAssignment(assignment);

                assertThat(result.getCategories()).hasSize(1);
                var remaining = result.getCategories().iterator().next();
                assertThat(remaining.getCategoryId()).isEqualTo(secondCategoryId);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate categories are unchanged")
            void originalAggregateIsUnchanged() {
                var original = specificationWithCategory(categoryId, true);
                original.removeCategoryAssignment(assignment);

                assertThat(original.getCategories()).hasSize(1);
            }

            @Test
            @DisplayName("Other fields are preserved after category removal")
            void otherFieldsPreserved() {
                var original = specificationWithCategory(categoryId, true);
                var result = original.removeCategoryAssignment(assignment);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationCategoryRemovedEvent")
            void registeredEventIsCorrectType() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationCategoryRemovedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecificationId() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the categoryId from assignment")
            void eventCarriesCategoryId() {
                var result = specificationWithCategory(categoryId, true)
                        .removeCategoryAssignment(assignment);
                var event = (ProductSpecificationCategoryRemovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getCategoryId()).isEqualTo(categoryId.value());
            }
        }
    }

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private SpecificationName newName;
        private SpecificationDescription newDescription;
        private SpecificationChangeInformationCommand changeCommand;

        @BeforeEach
        void setUpChangeCommand() {
            newName = new SpecificationName("Size Spec");
            newDescription = new SpecificationDescription("Defines the size attribute of a product");
            changeCommand = SpecificationChangeInformationCommand.builder()
                    .productSpecificationId(ProductSpecificationId.random())
                    .name(newName)
                    .description(newDescription)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates name from command")
            void updatesName() {
                var result = emptySpecification().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getName().value()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Updates description from command")
            void updatesDescription() {
                var result = emptySpecification().changeInformation(changeCommand);

                assertThat(result.getDescription()).isEqualTo(newDescription);
                assertThat(result.getDescription().value()).isEqualTo(newDescription.value());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = emptySpecification().changeInformation(changeCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = emptySpecification();
                original.changeInformation(changeCommand);

                assertThat(original.getName()).isEqualTo(name);
                assertThat(original.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Categories are preserved after information change")
            void categoriesPreserved() {
                var categoryId = ProductCategoryId.from(UUID.randomUUID());
                var specWithCategory = specificationWithCategory(categoryId, true);

                var result = specWithCategory.changeInformation(changeCommand);

                assertThat(result.getCategories()).hasSize(1);
                assertThat(result.getCategories().iterator().next().getCategoryId()).isEqualTo(categoryId);
            }

            @Test
            @DisplayName("ID is preserved after information change")
            void idPreserved() {
                var original = emptySpecification();
                var result = original.changeInformation(changeCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = emptySpecification().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationInformationChangedEvent")
            void registeredEventIsCorrectType() {
                var result = emptySpecification().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationInformationChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecificationId() {
                var result = emptySpecification().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = emptySpecification().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = emptySpecification().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new name")
            void eventCarriesNewName() {
                var result = emptySpecification().changeInformation(changeCommand);
                var event = (ProductSpecificationInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Event carries the new description")
            void eventCarriesNewDescription() {
                var result = emptySpecification().changeInformation(changeCommand);
                var event = (ProductSpecificationInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(newDescription.value());
            }
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var spec = emptySpecification();

            assertThat(spec.getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = SpecificationCreateCommand.builder()
                    .name(new SpecificationName("Weight Spec"))
                    .description(new SpecificationDescription("Defines the weight attribute of a product"))
                    .build();

            var first = ProductSpecificationRoot.initialize(createCommand);
            var second = ProductSpecificationRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }

        @Test
        @DisplayName("Sequential operations return new instances each time")
        void sequentialOperationsReturnNewInstances() {
            var categoryId = ProductCategoryId.from(UUID.randomUUID());
            var assignment = SpecificationCategoryAssignment.initialize(categoryId, true);

            var spec = emptySpecification();
            var afterAssign = spec.assignCategory(assignment);
            var afterRemove = afterAssign.removeCategoryAssignment(assignment);

            assertThat(spec.getCategories()).isEmpty();
            assertThat(afterAssign.getCategories()).hasSize(1);
            assertThat(afterRemove.getCategories()).isEmpty();
        }
    }
}