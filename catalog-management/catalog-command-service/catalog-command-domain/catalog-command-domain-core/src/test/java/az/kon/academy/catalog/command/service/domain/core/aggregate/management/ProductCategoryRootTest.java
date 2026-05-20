package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
import az.kon.academy.catalog.event.management.category.ProductCategoryCreatedEvent;
import az.kon.academy.catalog.event.management.category.ProductCategoryImageChangedEvent;
import az.kon.academy.catalog.event.management.category.ProductCategoryInformationChangedEvent;
import az.kon.academy.catalog.event.management.category.ProductCategoryParentChangedEvent;
import az.kon.academy.catalog.event.management.category.ProductCategoryParentRemovedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductCategoryRoot")
class ProductCategoryRootTest {

    private ProductCategoryName name;
    private ProductCategoryPath path;
    private ProductCategoryDescription description;
    private ProductCategoryCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        name = new ProductCategoryName("Electronics");
        path = new ProductCategoryPath("electronics");
        description = new ProductCategoryDescription("All kinds of electronic devices");
        createCommand = ProductCategoryCreateCommand.builder()
                .name(name)
                .path(path)
                .description(description)
                .build();
    }

    private ProductCategoryRoot freshCategory() {
        return ProductCategoryRoot.initialize(createCommand);
    }

    private ProductCategoryRoot categoryWithParent(ProductCategoryId parentId) {
        return freshCategory().changeParent(parentId);
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
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getRootID()).isNotNull();
                assertThat(category.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductCategoryRoot.initialize(createCommand);
                var second = ProductCategoryRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getName()).isEqualTo(name);
                assertThat(category.getName().value()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Sets path from command")
            void setsPathFromCommand() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getPath()).isEqualTo(path);
                assertThat(category.getPath().value()).isEqualTo(path.value());
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescriptionFromCommand() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getDescription()).isEqualTo(description);
                assertThat(category.getDescription().value()).isEqualTo(description.value());
            }

            @Test
            @DisplayName("Parent is null after initialization")
            void parentIsNull() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getParent()).isNull();
            }

            @Test
            @DisplayName("Image is null after initialization")
            void imageIsNull() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getImage()).isNull();
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryCreatedEvent")
            void registeredEventIsCorrectType() {
                var category = ProductCategoryRoot.initialize(createCommand);

                assertThat(category.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the category ID")
            void eventAggregateIdMatchesCategoryId() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = category.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(category.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = category.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = category.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(category.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the name from command")
            void eventCarriesName() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = (ProductCategoryCreatedEvent) category.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Event carries the path from command")
            void eventCarriesPath() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = (ProductCategoryCreatedEvent) category.getUncommittedEvents().getFirst();

                assertThat(event.getPath()).isEqualTo(path.value());
            }

            @Test
            @DisplayName("Event carries the description from command")
            void eventCarriesDescription() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = (ProductCategoryCreatedEvent) category.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(description.value());
            }

            @Test
            @DisplayName("Event image is null when no image is set")
            void eventImageIsNullWhenNoImageSet() {
                var category = ProductCategoryRoot.initialize(createCommand);
                var event = (ProductCategoryCreatedEvent) category.getUncommittedEvents().getFirst();

                assertThat(event.getImage()).isNull();
            }
        }
    }

    @Nested
    @DisplayName("changeImage()")
    class ChangeImage {

        private String newImage;
        private ProductCategoryChangeImageCommand imageCommand;

        @BeforeEach
        void setUpImageCommand() {
            newImage = "https://cdn.example.com/categories/electronics.png";
            imageCommand = ProductCategoryChangeImageCommand.builder()
                    .productCategoryId(ProductCategoryId.random())
                    .image(newImage)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates image from command")
            void updatesImage() {
                var result = freshCategory().changeImage(imageCommand);

                assertThat(result.getImage()).isEqualTo(newImage);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshCategory().changeImage(imageCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate image is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshCategory();
                original.changeImage(imageCommand);

                assertThat(original.getImage()).isNull();
            }

            @Test
            @DisplayName("Other fields are preserved after image change")
            void otherFieldsPreserved() {
                var original = freshCategory();
                var result = original.changeImage(imageCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getPath()).isEqualTo(original.getPath());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshCategory().changeImage(imageCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryImageChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshCategory().changeImage(imageCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryImageChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the category ID")
            void eventAggregateIdMatchesCategoryId() {
                var result = freshCategory().changeImage(imageCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshCategory().changeImage(imageCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshCategory().changeImage(imageCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new image")
            void eventCarriesNewImage() {
                var result = freshCategory().changeImage(imageCommand);
                var event = (ProductCategoryImageChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getImage()).isEqualTo(newImage);
            }
        }
    }

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private ProductCategoryName newName;
        private ProductCategoryPath newPath;
        private ProductCategoryDescription newDescription;
        private ProductCategoryChangeInformationCommand changeCommand;

        @BeforeEach
        void setUpChangeCommand() {
            newName = new ProductCategoryName("Mobile Phones");
            newPath = new ProductCategoryPath("mobile-phones");
            newDescription = new ProductCategoryDescription("Smartphones and mobile devices");
            changeCommand = ProductCategoryChangeInformationCommand.builder()
                    .productCategoryId(ProductCategoryId.random())
                    .name(newName)
                    .path(newPath)
                    .description(newDescription)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates name from command")
            void updatesName() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getName().value()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Updates path from command")
            void updatesPath() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getPath()).isEqualTo(newPath);
                assertThat(result.getPath().value()).isEqualTo(newPath.value());
            }

            @Test
            @DisplayName("Updates description from command")
            void updatesDescription() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getDescription()).isEqualTo(newDescription);
                assertThat(result.getDescription().value()).isEqualTo(newDescription.value());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshCategory();
                original.changeInformation(changeCommand);

                assertThat(original.getName()).isEqualTo(name);
                assertThat(original.getPath()).isEqualTo(path);
                assertThat(original.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("ID is preserved after information change")
            void idPreserved() {
                var original = freshCategory();
                var result = original.changeInformation(changeCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("Image is preserved after information change")
            void imagePreserved() {
                var imageCommand = ProductCategoryChangeImageCommand.builder()
                        .productCategoryId(ProductCategoryId.random())
                        .image("https://cdn.example.com/img.png")
                        .build();
                var withImage = freshCategory().changeImage(imageCommand);
                var result = withImage.changeInformation(changeCommand);

                assertThat(result.getImage()).isEqualTo(withImage.getImage());
            }

            @Test
            @DisplayName("Parent is preserved after information change")
            void parentPreserved() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var withParent = categoryWithParent(parentId);
                var result = withParent.changeInformation(changeCommand);

                assertThat(result.getParent()).isEqualTo(parentId);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryInformationChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshCategory().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryInformationChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the category ID")
            void eventAggregateIdMatchesCategoryId() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new name")
            void eventCarriesNewName() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = (ProductCategoryInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
            }

            @Test
            @DisplayName("Event carries the new path")
            void eventCarriesNewPath() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = (ProductCategoryInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getPath()).isEqualTo(newPath.value());
            }

            @Test
            @DisplayName("Event carries the new description")
            void eventCarriesNewDescription() {
                var result = freshCategory().changeInformation(changeCommand);
                var event = (ProductCategoryInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(newDescription.value());
            }
        }
    }

    @Nested
    @DisplayName("changeParent()")
    class ChangeParent {

        private ProductCategoryId parentId;

        @BeforeEach
        void setUpParentId() {
            parentId = ProductCategoryId.from(UUID.randomUUID());
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets parent from argument")
            void setsParent() {
                var result = freshCategory().changeParent(parentId);

                assertThat(result.getParent()).isEqualTo(parentId);
                assertThat(result.getParent().value()).isEqualTo(parentId.value());
            }

            @Test
            @DisplayName("Replaces existing parent with new one")
            void replacesExistingParent() {
                var firstParentId = ProductCategoryId.from(UUID.randomUUID());
                var secondParentId = ProductCategoryId.from(UUID.randomUUID());

                var result = freshCategory()
                        .changeParent(firstParentId)
                        .changeParent(secondParentId);

                assertThat(result.getParent()).isEqualTo(secondParentId);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshCategory().changeParent(parentId);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate parent is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshCategory();
                original.changeParent(parentId);

                assertThat(original.getParent()).isNull();
            }

            @Test
            @DisplayName("Other fields are preserved after parent change")
            void otherFieldsPreserved() {
                var original = freshCategory();
                var result = original.changeParent(parentId);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getPath()).isEqualTo(original.getPath());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshCategory().changeParent(parentId);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryParentChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshCategory().changeParent(parentId);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryParentChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the category ID")
            void eventAggregateIdMatchesCategoryId() {
                var result = freshCategory().changeParent(parentId);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshCategory().changeParent(parentId);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshCategory().changeParent(parentId);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the new parentId")
            void eventCarriesParentId() {
                var result = freshCategory().changeParent(parentId);
                var event = (ProductCategoryParentChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getParentId()).isEqualTo(parentId.value());
            }
        }
    }

    @Nested
    @DisplayName("removeParent()")
    class RemoveParent {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Parent becomes null after removal")
            void parentIsNullAfterRemoval() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();

                assertThat(result.getParent()).isNull();
            }

            @Test
            @DisplayName("Calling removeParent on category without parent keeps parent null")
            void removingNonExistentParentKeepsNull() {
                var result = freshCategory().removeParent();

                assertThat(result.getParent()).isNull();
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate parent is unchanged")
            void originalAggregateIsUnchanged() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var original = categoryWithParent(parentId);
                original.removeParent();

                assertThat(original.getParent()).isEqualTo(parentId);
            }

            @Test
            @DisplayName("Other fields are preserved after parent removal")
            void otherFieldsPreserved() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var original = categoryWithParent(parentId);
                var result = original.removeParent();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getPath()).isEqualTo(original.getPath());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryParentRemovedEvent")
            void registeredEventIsCorrectType() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryParentRemovedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the category ID")
            void eventAggregateIdMatchesCategoryId() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var parentId = ProductCategoryId.from(UUID.randomUUID());
                var result = categoryWithParent(parentId).removeParent();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Uncommitted events list is unmodifiable")
        void uncommittedEventsListIsUnmodifiable() {
            var category = freshCategory();

            assertThat(category.getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = ProductCategoryCreateCommand.builder()
                    .name(new ProductCategoryName("Clothing"))
                    .path(new ProductCategoryPath("clothing"))
                    .description(new ProductCategoryDescription("All kinds of clothing items"))
                    .build();

            var first = ProductCategoryRoot.initialize(createCommand);
            var second = ProductCategoryRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }

        @Test
        @DisplayName("Sequential operations return new instances each time")
        void sequentialOperationsReturnNewInstances() {
            var parentId = ProductCategoryId.from(UUID.randomUUID());

            var original = freshCategory();
            var withParent = original.changeParent(parentId);
            var withoutParent = withParent.removeParent();

            assertThat(original.getParent()).isNull();
            assertThat(withParent.getParent()).isEqualTo(parentId);
            assertThat(withoutParent.getParent()).isNull();
        }
    }
}