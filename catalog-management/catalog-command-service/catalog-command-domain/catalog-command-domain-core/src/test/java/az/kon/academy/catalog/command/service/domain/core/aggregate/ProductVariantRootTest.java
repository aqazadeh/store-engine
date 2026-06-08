package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductVariantDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantSku;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantStatus;
import az.kon.academy.catalog.event.productvariant.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductVariantRoot")
class ProductVariantRootTest {

    private ProductId productId;
    private ProductVariantAssignment assignment;
    private Barcode barcode;
    private ProductVariantSku sku;
    private ProductVariantAddCommand addCommand;
    private String image;

    @BeforeEach
    void setUp() {
        productId = ProductId.from(UUID.randomUUID());
        assignment = ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random());
        barcode = Barcode.of("1234567890123");
        sku = ProductVariantSku.of("SKU-TEST-001");
        image = "https://cdn.example.com/variant/image.png";
        addCommand = ProductVariantAddCommand.builder()
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .build();
    }

    private ProductVariantRoot variantInDraft() {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.DRAFT)
                .build();
    }

    private ProductVariantRoot variantInActive() {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.ACTIVE)
                .build();
    }

    private ProductVariantRoot variantInInactive() {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.INACTIVE)
                .build();
    }

    private ProductVariantRoot variantInOutOfStock() {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.OUT_OF_STOCK)
                .build();
    }

    private ProductVariantRoot variantInDiscontinued() {
        return ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(productId)
                .assignments(List.of(assignment))
                .barcode(barcode)
                .sku(sku)
                .images(List.of())
                .status(ProductVariantStatus.DISCONTINUED)
                .build();
    }

    // ═════════════════════════════════════════════════════════════════════
    // initialize
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("initialize()")
    class Initialize {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getRootID()).isNotNull();
                assertThat(variant.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductVariantRoot.initialize(addCommand);
                var second = ProductVariantRoot.initialize(addCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets productId from command")
            void setsProductIdFromCommand() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getProductId()).isEqualTo(productId);
            }

            @Test
            @DisplayName("Sets assignments from command")
            void setsAssignmentsFromCommand() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getAssignments()).containsExactlyElementsOf(List.of(assignment));
            }

            @Test
            @DisplayName("Sets barcode from command")
            void setsBarcodeFromCommand() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getBarcode()).isEqualTo(barcode);
            }

            @Test
            @DisplayName("Sets sku from command")
            void setsSkuFromCommand() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getSku()).isEqualTo(sku);
            }

            @Test
            @DisplayName("Images is empty list")
            void imagesIsEmpty() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getImages()).isEmpty();
            }

            @Test
            @DisplayName("Status is DRAFT")
            void statusIsDraft() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getStatus()).isEqualTo(ProductVariantStatus.DRAFT);
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantAddedEvent")
            void registeredEventIsProductVariantAddedEvent() {
                var variant = ProductVariantRoot.initialize(addCommand);

                assertThat(variant.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantAddedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = variant.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(variant.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = variant.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = variant.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(variant.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event status is DRAFT")
            void eventStatusIsDraft() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = (ProductVariantAddedEvent) variant.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductVariantStatus.DRAFT.name());
            }

            @Test
            @DisplayName("Event barcode matches variant barcode")
            void eventBarcodeMatches() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = (ProductVariantAddedEvent) variant.getUncommittedEvents().getFirst();

                assertThat(event.getBarcode()).isEqualTo(barcode.value());
            }

            @Test
            @DisplayName("Event sku matches variant sku")
            void eventSkuMatches() {
                var variant = ProductVariantRoot.initialize(addCommand);
                var event = (ProductVariantAddedEvent) variant.getUncommittedEvents().getFirst();

                assertThat(event.getSku()).isEqualTo(sku.value());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeBarcode
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeBarcode()")
    class ChangeBarcode {

        private Barcode newBarcode;

        @BeforeEach
        void setUp() {
            newBarcode = Barcode.of("9876543210987");
        }

        @Nested
        @DisplayName("When status is DRAFT")
        class WhenStatusIsDraft {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("Updates barcode")
                void updatesBarcode() {
                    var result = variantInDraft().changeBarcode(newBarcode);

                    assertThat(result.getBarcode()).isEqualTo(newBarcode);
                }

                @Test
                @DisplayName("modificationTs is updated")
                void modificationTsIsUpdated() {
                    var result = variantInDraft().changeBarcode(newBarcode);

                    assertThat(result.getModificationTs()).isNotNull();
                }

                @Test
                @DisplayName("Original aggregate is unchanged")
                void originalAggregateIsUnchanged() {
                    var original = variantInDraft();
                    original.changeBarcode(newBarcode);

                    assertThat(original.getBarcode()).isEqualTo(barcode);
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var original = variantInDraft();
                    var result = original.changeBarcode(newBarcode);

                    assertThat(result.getRootID()).isEqualTo(original.getRootID());
                    assertThat(result.getProductId()).isEqualTo(original.getProductId());
                    assertThat(result.getSku()).isEqualTo(original.getSku());
                    assertThat(result.getStatus()).isEqualTo(original.getStatus());
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = variantInDraft().changeBarcode(newBarcode);

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductVariantBarcodeChangedEvent")
                void registeredEventIsBarcodeChangedEvent() {
                    var result = variantInDraft().changeBarcode(newBarcode);

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductVariantBarcodeChangedEvent.class);
                }

                @Test
                @DisplayName("Event barcode matches new barcode")
                void eventBarcodeMatches() {
                    var result = variantInDraft().changeBarcode(newBarcode);
                    var event = (ProductVariantBarcodeChangedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getBarcode()).isEqualTo(newBarcode.value());
                }

                @Test
                @DisplayName("Event aggregateId matches variant ID")
                void eventAggregateIdMatchesVariantId() {
                    var result = variantInDraft().changeBarcode(newBarcode);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is ACTIVE")
            void throwsWhenActive() {
                assertThatThrownBy(() -> variantInActive().changeBarcode(newBarcode))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is INACTIVE")
            void throwsWhenInactive() {
                assertThatThrownBy(() -> variantInInactive().changeBarcode(newBarcode))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is OUT_OF_STOCK")
            void throwsWhenOutOfStock() {
                assertThatThrownBy(() -> variantInOutOfStock().changeBarcode(newBarcode))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is DISCONTINUED")
            void throwsWhenDiscontinued() {
                assertThatThrownBy(() -> variantInDiscontinued().changeBarcode(newBarcode))
                        .isInstanceOf(ProductVariantDomainException.class);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // addImage
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("addImage()")
    class AddImage {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds image to images list")
            void addsImageToList() {
                var result = variantInDraft().addImage(image);

                assertThat(result.getImages()).contains(image);
            }

            @Test
            @DisplayName("Appends image to existing images")
            void appendsImageToExisting() {
                var variant = variantInDraft();
                var first = variant.addImage("image-1.png");
                var second = first.addImage("image-2.png");

                assertThat(second.getImages()).containsExactly("image-1.png", "image-2.png");
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInDraft().addImage(image);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInDraft();
                original.addImage(image);

                assertThat(original.getImages()).isEmpty();
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInDraft();
                var result = original.addImage(image);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
                assertThat(result.getStatus()).isEqualTo(original.getStatus());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInDraft().addImage(image);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantImageAddedEvent")
            void registeredEventIsImageAddedEvent() {
                var result = variantInDraft().addImage(image);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantImageAddedEvent.class);
            }

            @Test
            @DisplayName("Event image matches added image")
            void eventImageMatches() {
                var result = variantInDraft().addImage(image);
                var event = (ProductVariantImageAddedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getImage()).isEqualTo(image);
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInDraft().addImage(image);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // removeImage
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("removeImage()")
    class RemoveImage {

        private ProductVariantRoot variantWithImages;

        @BeforeEach
        void setUp() {
            variantWithImages = ProductVariantRoot.builder()
                    .id(ProductVariantId.random())
                    .productId(productId)
                    .assignments(List.of(assignment))
                    .barcode(barcode)
                    .sku(sku)
                    .images(List.of("img-1.png", "img-2.png", "img-3.png"))
                    .status(ProductVariantStatus.DRAFT)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Removes image from list")
            void removesImageFromList() {
                var result = variantWithImages.removeImage("img-2.png");

                assertThat(result.getImages()).containsExactly("img-1.png", "img-3.png");
            }

            @Test
            @DisplayName("Does nothing when image not found")
            void doesNothingWhenImageNotFound() {
                var result = variantWithImages.removeImage("non-existent.png");

                assertThat(result.getImages()).containsExactly("img-1.png", "img-2.png", "img-3.png");
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantWithImages.removeImage("img-1.png");

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantWithImages;
                original.removeImage("img-1.png");

                assertThat(original.getImages()).contains("img-1.png");
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantWithImages.removeImage("img-1.png");

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantImageRemovedEvent")
            void registeredEventIsImageRemovedEvent() {
                var result = variantWithImages.removeImage("img-1.png");

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantImageRemovedEvent.class);
            }

            @Test
            @DisplayName("Event image matches removed image")
            void eventImageMatches() {
                var result = variantWithImages.removeImage("img-1.png");
                var event = (ProductVariantImageRemovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getImage()).isEqualTo("img-1.png");
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantWithImages.removeImage("img-1.png");
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // markImagePrimary
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("markImagePrimary()")
    class MarkImagePrimary {

        private ProductVariantRoot variantWithImages;

        @BeforeEach
        void setUp() {
            variantWithImages = ProductVariantRoot.builder()
                    .id(ProductVariantId.random())
                    .productId(productId)
                    .assignments(List.of(assignment))
                    .barcode(barcode)
                    .sku(sku)
                    .images(List.of("img-1.png", "img-2.png", "img-3.png"))
                    .status(ProductVariantStatus.DRAFT)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Moves image to first position")
            void movesImageToFirst() {
                var result = variantWithImages.markImagePrimary("img-3.png");

                assertThat(result.getImages()).containsExactly("img-3.png", "img-1.png", "img-2.png");
            }

            @Test
            @DisplayName("Already first image stays first")
            void alreadyFirstStaysFirst() {
                var result = variantWithImages.markImagePrimary("img-1.png");

                assertThat(result.getImages()).containsExactly("img-1.png", "img-2.png", "img-3.png");
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantWithImages.markImagePrimary("img-2.png");

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantWithImages;
                original.markImagePrimary("img-3.png");

                assertThat(original.getImages().getFirst()).isEqualTo("img-1.png");
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantWithImages.markImagePrimary("img-2.png");

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantImageMarkedAsPrimaryEvent")
            void registeredEventIsMarkedAsPrimaryEvent() {
                var result = variantWithImages.markImagePrimary("img-2.png");

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantImageMarkedAsPrimaryEvent.class);
            }

            @Test
            @DisplayName("Event image matches primary image")
            void eventImageMatches() {
                var result = variantWithImages.markImagePrimary("img-2.png");
                var event = (ProductVariantImageMarkedAsPrimaryEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getImage()).isEqualTo("img-2.png");
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantWithImages.markImagePrimary("img-2.png");
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // activate
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("activate()")
    class Activate {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status becomes ACTIVE")
            void statusBecomesActive() {
                var result = variantInDraft().activate();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("Works from INACTIVE")
            void worksFromInactive() {
                var result = variantInInactive().activate();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("Works from OUT_OF_STOCK")
            void worksFromOutOfStock() {
                var result = variantInOutOfStock().activate();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInDraft().activate();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInDraft();
                original.activate();

                assertThat(original.getStatus()).isEqualTo(ProductVariantStatus.DRAFT);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInDraft();
                var result = original.activate();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInDraft().activate();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantActivatedEvent")
            void registeredEventIsActivatedEvent() {
                var result = variantInDraft().activate();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantActivatedEvent.class);
            }

            @Test
            @DisplayName("Event status is ACTIVE")
            void eventStatusIsActive() {
                var result = variantInDraft().activate();
                var event = (ProductVariantActivatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE.name());
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInDraft().activate();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // deactivate
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("deactivate()")
    class Deactivate {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status becomes INACTIVE")
            void statusBecomesInactive() {
                var result = variantInActive().deactivate();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.INACTIVE);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInActive().deactivate();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInActive();
                original.deactivate();

                assertThat(original.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInActive();
                var result = original.deactivate();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInActive().deactivate();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantDeactivatedEvent")
            void registeredEventIsDeactivatedEvent() {
                var result = variantInActive().deactivate();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantDeactivatedEvent.class);
            }

            @Test
            @DisplayName("Event status is INACTIVE")
            void eventStatusIsInactive() {
                var result = variantInActive().deactivate();
                var event = (ProductVariantDeactivatedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductVariantStatus.INACTIVE.name());
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInActive().deactivate();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // markOutOfStock
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("markOutOfStock()")
    class MarkOutOfStock {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status becomes OUT_OF_STOCK")
            void statusBecomesOutOfStock() {
                var result = variantInActive().markOutOfStock();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.OUT_OF_STOCK);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInActive().markOutOfStock();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInActive();
                original.markOutOfStock();

                assertThat(original.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInActive();
                var result = original.markOutOfStock();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInActive().markOutOfStock();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantMarkedOutOfStockEvent")
            void registeredEventIsMarkedOutOfStockEvent() {
                var result = variantInActive().markOutOfStock();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantMarkedOutOfStockEvent.class);
            }

            @Test
            @DisplayName("Event status is OUT_OF_STOCK")
            void eventStatusIsOutOfStock() {
                var result = variantInActive().markOutOfStock();
                var event = (ProductVariantMarkedOutOfStockEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductVariantStatus.OUT_OF_STOCK.name());
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInActive().markOutOfStock();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // discontinue
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("discontinue()")
    class Discontinue {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status becomes DISCONTINUED")
            void statusBecomesDiscontinued() {
                var result = variantInActive().discontinue();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.DISCONTINUED);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInActive().discontinue();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInActive();
                original.discontinue();

                assertThat(original.getStatus()).isEqualTo(ProductVariantStatus.ACTIVE);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInActive();
                var result = original.discontinue();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInActive().discontinue();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantDiscontinuedEvent")
            void registeredEventIsDiscontinuedEvent() {
                var result = variantInActive().discontinue();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantDiscontinuedEvent.class);
            }

            @Test
            @DisplayName("Event status is DISCONTINUED")
            void eventStatusIsDiscontinued() {
                var result = variantInActive().discontinue();
                var event = (ProductVariantDiscontinuedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductVariantStatus.DISCONTINUED.name());
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInActive().discontinue();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // archive
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("archive()")
    class Archive {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status becomes ARCHIVED")
            void statusBecomesArchived() {
                var result = variantInDiscontinued().archive();

                assertThat(result.getStatus()).isEqualTo(ProductVariantStatus.ARCHIVED);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = variantInDiscontinued().archive();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = variantInDiscontinued();
                original.archive();

                assertThat(original.getStatus()).isEqualTo(ProductVariantStatus.DISCONTINUED);
            }

            @Test
            @DisplayName("Other fields are preserved")
            void otherFieldsPreserved() {
                var original = variantInDiscontinued();
                var result = original.archive();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getProductId()).isEqualTo(original.getProductId());
                assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = variantInDiscontinued().archive();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantArchivedEvent")
            void registeredEventIsArchivedEvent() {
                var result = variantInDiscontinued().archive();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantArchivedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches variant ID")
            void eventAggregateIdMatchesVariantId() {
                var result = variantInDiscontinued().archive();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // changeSku
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("changeSku()")
    class ChangeSku {

        private ProductVariantSku newSku;

        @BeforeEach
        void setUp() {
            newSku = ProductVariantSku.of("SKU-NEW-001");
        }

        @Nested
        @DisplayName("When status is DRAFT")
        class WhenStatusIsDraft {

            @Nested
            @DisplayName("Aggregate state")
            class AggregateState {

                @Test
                @DisplayName("Updates sku")
                void updatesSku() {
                    var result = variantInDraft().changeSku(newSku);

                    assertThat(result.getSku()).isEqualTo(newSku);
                }

                @Test
                @DisplayName("modificationTs is updated")
                void modificationTsIsUpdated() {
                    var result = variantInDraft().changeSku(newSku);

                    assertThat(result.getModificationTs()).isNotNull();
                }

                @Test
                @DisplayName("Original aggregate is unchanged")
                void originalAggregateIsUnchanged() {
                    var original = variantInDraft();
                    original.changeSku(newSku);

                    assertThat(original.getSku()).isEqualTo(sku);
                }

                @Test
                @DisplayName("Other fields are preserved")
                void otherFieldsPreserved() {
                    var original = variantInDraft();
                    var result = original.changeSku(newSku);

                    assertThat(result.getRootID()).isEqualTo(original.getRootID());
                    assertThat(result.getProductId()).isEqualTo(original.getProductId());
                    assertThat(result.getBarcode()).isEqualTo(original.getBarcode());
                    assertThat(result.getStatus()).isEqualTo(original.getStatus());
                }
            }

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = variantInDraft().changeSku(newSku);

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductVariantSkuChangedEvent")
                void registeredEventIsSkuChangedEvent() {
                    var result = variantInDraft().changeSku(newSku);

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductVariantSkuChangedEvent.class);
                }

                @Test
                @DisplayName("Event sku matches new sku")
                void eventSkuMatches() {
                    var result = variantInDraft().changeSku(newSku);
                    var event = (ProductVariantSkuChangedEvent) result.getUncommittedEvents().getFirst();

                    assertThat(event.getSku()).isEqualTo(newSku.value());
                }

                @Test
                @DisplayName("Event aggregateId matches variant ID")
                void eventAggregateIdMatchesVariantId() {
                    var result = variantInDraft().changeSku(newSku);
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is ACTIVE")
            void throwsWhenActive() {
                assertThatThrownBy(() -> variantInActive().changeSku(newSku))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is INACTIVE")
            void throwsWhenInactive() {
                assertThatThrownBy(() -> variantInInactive().changeSku(newSku))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is OUT_OF_STOCK")
            void throwsWhenOutOfStock() {
                assertThatThrownBy(() -> variantInOutOfStock().changeSku(newSku))
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is DISCONTINUED")
            void throwsWhenDiscontinued() {
                assertThatThrownBy(() -> variantInDiscontinued().changeSku(newSku))
                        .isInstanceOf(ProductVariantDomainException.class);
            }
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    // remove
    // ═════════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("remove()")
    class Remove {

        @Nested
        @DisplayName("When status is DRAFT")
        class WhenStatusIsDraft {

            @Nested
            @DisplayName("Event publishing")
            class EventPublishing {

                @Test
                @DisplayName("Registers exactly one uncommitted event")
                void registersExactlyOneEvent() {
                    var result = variantInDraft().remove();

                    assertThat(result.getUncommittedEvents()).hasSize(1);
                }

                @Test
                @DisplayName("Registered event is ProductVariantRemovedEvent")
                void registeredEventIsRemovedEvent() {
                    var result = variantInDraft().remove();

                    assertThat(result.getUncommittedEvents().getFirst())
                            .isInstanceOf(ProductVariantRemovedEvent.class);
                }

                @Test
                @DisplayName("Event aggregateId matches variant ID")
                void eventAggregateIdMatchesVariantId() {
                    var result = variantInDraft().remove();
                    var event = result.getUncommittedEvents().getFirst();

                    assertThat(event.getAggregateId())
                            .isEqualTo(result.getRootID().value().toString());
                }
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is ACTIVE")
            void throwsWhenActive() {
                assertThatThrownBy(() -> variantInActive().remove())
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is INACTIVE")
            void throwsWhenInactive() {
                assertThatThrownBy(() -> variantInInactive().remove())
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is OUT_OF_STOCK")
            void throwsWhenOutOfStock() {
                assertThatThrownBy(() -> variantInOutOfStock().remove())
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is DISCONTINUED")
            void throwsWhenDiscontinued() {
                assertThatThrownBy(() -> variantInDiscontinued().remove())
                        .isInstanceOf(ProductVariantDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is ARCHIVED")
            void throwsWhenArchived() {
                var archived = variantInDiscontinued().archive();
                assertThatThrownBy(() -> ProductVariantRoot.builder()
                        .id(archived.getRootID())
                        .productId(archived.getProductId())
                        .assignments(archived.getAssignments())
                        .barcode(archived.getBarcode())
                        .sku(archived.getSku())
                        .images(archived.getImages())
                        .status(ProductVariantStatus.ARCHIVED)
                        .build().remove())
                        .isInstanceOf(ProductVariantDomainException.class);
            }
        }
    }
}
