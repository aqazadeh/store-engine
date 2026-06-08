package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryAssignedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCategoryRemovedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationCreatedEvent;
import az.kon.academy.catalog.event.management.specification.ProductSpecificationDeletedEvent;
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
    private ProductSpecificationCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        name = new SpecificationName("Screen Size");
        description = new SpecificationDescription("Display diagonal size in inches");
        createCommand = ProductSpecificationCreateCommand.builder()
                .name(name)
                .description(description)
                .build();
    }

    private ProductSpecificationRoot freshSpec() {
        return ProductSpecificationRoot.initialize(createCommand);
    }

    private ProductSpecificationRoot specWithCategory(ProductCategoryId categoryId, boolean isRequired) {
        var command = ProductSpecificationAssignCategoryCommand.builder()
                .productSpecificationId(ProductSpecificationId.random())
                .categoryId(categoryId)
                .isRequired(isRequired)
                .build();
        return freshSpec().assignCategory(command);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initialize
    // ─────────────────────────────────────────────────────────────────────────

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
            @DisplayName("Categories is empty after initialization")
            void categoriesIsEmpty() {
                var spec = ProductSpecificationRoot.initialize(createCommand);

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
            void eventAggregateIdMatchesSpecId() {
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

    // ─────────────────────────────────────────────────────────────────────────
    // assignCategory
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignCategory()")
    class AssignCategory {

        private ProductCategoryId categoryId;
        private ProductSpecificationAssignCategoryCommand assignCommand;

        @BeforeEach
        void setUpAssignCommand() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            assignCommand = ProductSpecificationAssignCategoryCommand.builder()
                    .productSpecificationId(ProductSpecificationId.random())
                    .categoryId(categoryId)
                    .isRequired(true)
                    .build();
        }

        @Nested
        @DisplayName("When category is not yet assigned")
        class WhenCategoryIsNotAssigned {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("Adds category to categories set")
                void addsCategoryToSet() {
                    var result = freshSpec().assignCategory(assignCommand);

                    assertThat(result.getCategories()).hasSize(1);
                    assertThat(result.getCategories().iterator().next().getCategoryId())
                            .isEqualTo(categoryId);
                }

                @Test
                @DisplayName("Assignment has correct required flag")
                void assignmentHasCorrectRequiredFlag() {
                    var result = freshSpec().assignCategory(assignCommand);

                    assertThat(result.getCategories().iterator().next().isRequired()).isTrue();
                }

                @Test
                @DisplayName("Assignment with required=false is stored correctly")
                void assignmentWithRequiredFalse() {
                    var optionalCommand = ProductSpecificationAssignCategoryCommand.builder()
                            .productSpecificationId(ProductSpecificationId.random())
                            .categoryId(categoryId)
                            .isRequired(false)
                            .build();
                    var result = freshSpec().assignCategory(optionalCommand);

                    assertThat(result.getCategories().iterator().next().isRequired()).isFalse();
                }

                @Test
                @DisplayName("modificationTs is updated")
                void modificationTsIsUpdated() {
                    var result = freshSpec().assignCategory(assignCommand);

                    assertThat(result.getModificationTs()).isNotNull();
                }

                @Test
                @DisplayName("Original aggregate categories unchanged")
                void originalAggregateIsUnchanged() {
                    var original = freshSpec();
                    original.assignCategory(assignCommand);

                    assertThat(original.getCategories()).isEmpty();
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var original = freshSpec();
                    var result = original.assignCategory(assignCommand);

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
                    var result = freshSpec().assignCategory(assignCommand);

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductSpecificationCategoryAssignedEvent")
                void registeredEventIsCorrectType() {
                    var result = freshSpec().assignCategory(assignCommand);

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductSpecificationCategoryAssignedEvent.class);
                }

                @Test
                @DisplayName("Event aggregateId matches the specification ID")
                void eventAggregateIdMatchesSpecId() {
                    var result = freshSpec().assignCategory(assignCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }

                @Test
                @DisplayName("Event has a non-null eventId")
                void eventHasNonNullEventId() {
                    var result = freshSpec().assignCategory(assignCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getEventId()).isNotNull();
                }

                @Test
                @DisplayName("Event timestamp matches aggregate modificationTs")
                void eventTimestampMatchesModificationTs() {
                    var result = freshSpec().assignCategory(assignCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getTimestamp())
                            .isEqualTo(result.getModificationTs().toOffsetDateTime());
                }

                @Test
                @DisplayName("Event carries the categoryId")
                void eventCarriesCategoryId() {
                    var result = freshSpec().assignCategory(assignCommand);
                    var event = (ProductSpecificationCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getCategoryId()).isEqualTo(categoryId.value());
                }

                @Test
                @DisplayName("Event carries the required flag")
                void eventCarriesRequiredFlag() {
                    var result = freshSpec().assignCategory(assignCommand);
                    var event = (ProductSpecificationCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.isRequired()).isTrue();
                }
            }
        }

        @Nested
        @DisplayName("When category is already assigned")
        class WhenCategoryIsAlreadyAssigned {

            @Test
            @DisplayName("Does not duplicate the assignment")
            void doesNotDuplicateAssignment() {
                var spec = specWithCategory(categoryId, true);
                var result = spec.assignCategory(assignCommand);

                assertThat(result.getCategories()).hasSize(1);
            }

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var spec = specWithCategory(categoryId, true);
                var result = spec.assignCategory(assignCommand);

                assertThat(result).isSameAs(spec);
            }

            @Test
            @DisplayName("Registers no additional events")
            void registersNoAdditionalEvents() {
                var spec = specWithCategory(categoryId, true);
                var result = spec.assignCategory(assignCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationCategoryAssignedEvent.class);
            }
        }

        @Nested
        @DisplayName("Multiple categories")
        class MultipleCategories {

            @Test
            @DisplayName("Can assign multiple different categories")
            void canAssignMultipleCategories() {
                var secondCategoryId = ProductCategoryId.from(UUID.randomUUID());
                var secondCommand = ProductSpecificationAssignCategoryCommand.builder()
                        .productSpecificationId(ProductSpecificationId.random())
                        .categoryId(secondCategoryId)
                        .isRequired(false)
                        .build();

                var result = freshSpec()
                        .assignCategory(assignCommand)
                        .assignCategory(secondCommand);

                assertThat(result.getCategories()).hasSize(2);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // removeCategoryAssignment
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("removeCategoryAssignment()")
    class RemoveCategoryAssignment {

        private ProductCategoryId categoryId;
        private ProductSpecificationRemoveCategoryAssignmentCommand removeCommand;

        @BeforeEach
        void setUpRemoveCommand() {
            categoryId = ProductCategoryId.from(UUID.randomUUID());
            removeCommand = ProductSpecificationRemoveCategoryAssignmentCommand.builder()
                    .productSpecificationId(ProductSpecificationId.random())
                    .categoryId(categoryId)
                    .build();
        }

        @Nested
        @DisplayName("When category is assigned")
        class WhenCategoryIsAssigned {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("Removes category from categories set")
                void removesCategoryFromSet() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);

                    assertThat(result.getCategories()).isEmpty();
                }

                @Test
                @DisplayName("modificationTs is updated")
                void modificationTsIsUpdated() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);

                    assertThat(result.getModificationTs()).isNotNull();
                }

                @Test
                @DisplayName("Original aggregate categories unchanged")
                void originalAggregateIsUnchanged() {
                    var spec = specWithCategory(categoryId, true);
                    spec.removeCategoryAssignment(removeCommand);

                    assertThat(spec.getCategories()).hasSize(1);
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);

                    assertThat(result.getRootID()).isEqualTo(spec.getRootID());
                    assertThat(result.getName()).isEqualTo(spec.getName());
                    assertThat(result.getDescription()).isEqualTo(spec.getDescription());
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductSpecificationCategoryRemovedEvent")
                void registeredEventIsCorrectType() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductSpecificationCategoryRemovedEvent.class);
                }

                @Test
                @DisplayName("Event aggregateId matches the specification ID")
                void eventAggregateIdMatchesSpecId() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }

                @Test
                @DisplayName("Event has a non-null eventId")
                void eventHasNonNullEventId() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getEventId()).isNotNull();
                }

                @Test
                @DisplayName("Event timestamp matches aggregate modificationTs")
                void eventTimestampMatchesModificationTs() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getTimestamp())
                            .isEqualTo(result.getModificationTs().toOffsetDateTime());
                }

                @Test
                @DisplayName("Event carries the categoryId")
                void eventCarriesCategoryId() {
                    var spec = specWithCategory(categoryId, true);
                    var result = spec.removeCategoryAssignment(removeCommand);
                    var event = (ProductSpecificationCategoryRemovedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getCategoryId()).isEqualTo(categoryId.value());
                }
            }
        }

        @Nested
        @DisplayName("When category is not assigned")
        class WhenCategoryIsNotAssigned {

            @Test
            @DisplayName("Categories set remains empty")
            void categoriesSetRemainsEmpty() {
                var result = freshSpec().removeCategoryAssignment(removeCommand);

                assertThat(result.getCategories()).isEmpty();
            }

            @Test
            @DisplayName("Returns the same aggregate unchanged")
            void returnsSameAggregateUnchanged() {
                var spec = freshSpec();
                var result = spec.removeCategoryAssignment(removeCommand);

                assertThat(result).isSameAs(spec);
            }

            @Test
            @DisplayName("Registers no additional events")
            void registersNoAdditionalEvents() {
                var result = freshSpec().removeCategoryAssignment(removeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationCreatedEvent.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private SpecificationName newName;
        private SpecificationDescription newDescription;
        private ProductSpecificationChangeInformationCommand changeCommand;

        @BeforeEach
        void setUpChangeCommand() {
            newName = new SpecificationName("RAM Size");
            newDescription = new SpecificationDescription("Random access memory size in gigabytes");
            changeCommand = ProductSpecificationChangeInformationCommand.builder()
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
                var result = freshSpec().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getName().value()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Updates description from command")
            void updatesDescription() {
                var result = freshSpec().changeInformation(changeCommand);

                assertThat(result.getDescription()).isEqualTo(newDescription);
                assertThat(result.getDescription().value()).isEqualTo(newDescription.value());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshSpec().changeInformation(changeCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshSpec();
                original.changeInformation(changeCommand);

                assertThat(original.getName()).isEqualTo(name);
                assertThat(original.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var original = freshSpec();
                var result = original.changeInformation(changeCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("Categories are preserved")
            void categoriesPreserved() {
                var categoryId = ProductCategoryId.from(UUID.randomUUID());
                var spec = specWithCategory(categoryId, true);
                var result = spec.changeInformation(changeCommand);

                assertThat(result.getCategories()).hasSize(1);
                assertThat(result.getCategories().iterator().next().getCategoryId()).isEqualTo(categoryId);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshSpec().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationInformationChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshSpec().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationInformationChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecId() {
                var result = freshSpec().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshSpec().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshSpec().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new name")
            void eventCarriesNewName() {
                var result = freshSpec().changeInformation(changeCommand);
                var event = (ProductSpecificationInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Event carries the new description")
            void eventCarriesNewDescription() {
                var result = freshSpec().changeInformation(changeCommand);
                var event = (ProductSpecificationInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(newDescription.value());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // delete
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("delete()")
    class Delete {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Row status becomes DELETED")
            void rowStatusIsDeleted() {
                var result = freshSpec().delete();

                assertThat(result.getRowStatus()).isEqualTo(RowStatus.DELETED);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshSpec().delete();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate row status is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshSpec();
                original.delete();

                assertThat(original.getRowStatus()).isEqualTo(RowStatus.ACTIVE);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = freshSpec();
                var result = original.delete();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }

            @Test
            @DisplayName("Categories are preserved")
            void categoriesPreserved() {
                var categoryId = ProductCategoryId.from(UUID.randomUUID());
                var spec = specWithCategory(categoryId, true);
                var result = spec.delete();

                assertThat(result.getCategories()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshSpec().delete();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationDeletedEvent")
            void registeredEventIsCorrectType() {
                var result = freshSpec().delete();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationDeletedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the specification ID")
            void eventAggregateIdMatchesSpecId() {
                var result = freshSpec().delete();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshSpec().delete();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshSpec().delete();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Immutability
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var spec = freshSpec();

            assertThat(spec.getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Categories set is unmodifiable after assignment")
        void categoriesSetIsUnmodifiableAfterAssignment() {
            var categoryId = ProductCategoryId.from(UUID.randomUUID());
            var spec = specWithCategory(categoryId, true);

            assertThat(spec.getCategories()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = ProductSpecificationCreateCommand.builder()
                    .name(new SpecificationName("Weight"))
                    .description(new SpecificationDescription("Product weight in kilograms"))
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

            var original = freshSpec();
            var withCategory = original.assignCategory(
                    ProductSpecificationAssignCategoryCommand.builder()
                            .productSpecificationId(ProductSpecificationId.random())
                            .categoryId(categoryId)
                            .isRequired(true)
                            .build()
            );
            var withoutCategory = withCategory.removeCategoryAssignment(
                    ProductSpecificationRemoveCategoryAssignmentCommand.builder()
                            .productSpecificationId(ProductSpecificationId.random())
                            .categoryId(categoryId)
                            .build()
            );

            assertThat(original.getCategories()).isEmpty();
            assertThat(withCategory.getCategories()).hasSize(1);
            assertThat(withoutCategory.getCategories()).isEmpty();
        }
    }
}
