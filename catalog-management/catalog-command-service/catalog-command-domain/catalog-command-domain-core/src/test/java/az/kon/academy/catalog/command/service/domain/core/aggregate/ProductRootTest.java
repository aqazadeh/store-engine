package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.*;
import az.kon.academy.catalog.event.product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductRoot")
class ProductRootTest {

    private MerchantId merchantId;
    private ProductCategoryId categoryId;
    private BrandId brandId;
    private ProductName name;
    private ProductDescription description;
    private Barcode barcode;
    private ProductCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        merchantId = MerchantId.random();
        categoryId = ProductCategoryId.random();
        brandId = BrandId.random();
        name = new ProductName("Gaming Laptop");
        description = new ProductDescription("A high-end gaming laptop for professionals");
        barcode = Barcode.of("1234567890123");
        createCommand = ProductCreateCommand.builder()
                .merchantId(merchantId)
                .categoryId(categoryId)
                .brandId(brandId)
                .name(name)
                .description(description)
                .barcode(barcode)
                .build();
    }

    private ProductRoot freshProduct() {
        return ProductRoot.initialize(createCommand);
    }

    private ProductRoot productInSentToApprovalState() {
        return freshProduct().sentToApproval();
    }

    private ProductRoot productInApprovedState() {
        return productInSentToApprovalState().approve();
    }

    private ProductRoot productInRejectedState() {
        return productInSentToApprovalState().reject();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initialize()
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
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getRootID()).isNotNull();
                assertThat(product.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = ProductRoot.initialize(createCommand);
                var second = ProductRoot.initialize(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets merchantId from command")
            void setsMerchantId() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getMerchantId()).isEqualTo(merchantId);
                assertThat(product.getMerchantId().value()).isEqualTo(merchantId.value());
            }

            @Test
            @DisplayName("Sets categoryId from command")
            void setsCategoryId() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getCategoryId()).isEqualTo(categoryId);
                assertThat(product.getCategoryId().value()).isEqualTo(categoryId.value());
            }

            @Test
            @DisplayName("Sets brandId from command")
            void setsBrandId() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getBrandId()).isEqualTo(brandId);
                assertThat(product.getBrandId().value()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsName() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getName()).isEqualTo(name);
                assertThat(product.getName().value()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescription() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getDescription()).isEqualTo(description);
                assertThat(product.getDescription().value()).isEqualTo(description.value());
            }

            @Test
            @DisplayName("Sets barcode from command")
            void setsBarcode() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getBarcode()).isEqualTo(barcode);
                assertThat(product.getBarcode().value()).isEqualTo(barcode.value());
            }

            @Test
            @DisplayName("autoPriceUpdateEnabled defaults to false")
            void autoPriceUpdateEnabledDefaultsFalse() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getAutoPriceUpdateEnabled()).isFalse();
            }

            @Test
            @DisplayName("Status defaults to DRAFT")
            void statusDefaultsDraft() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getStatus()).isEqualTo(ProductStatus.DRAFT);
            }

            @Test
            @DisplayName("Specifications initialized as empty list")
            void specificationsInitializedEmpty() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getSpecifications()).isEmpty();
            }

            @Test
            @DisplayName("Variants initialized as empty list")
            void variantsInitializedEmpty() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getVariants()).isEmpty();
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCreatedEvent")
            void registeredEventIsCorrectType() {
                var product = ProductRoot.initialize(createCommand);

                assertThat(product.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the product ID")
            void eventAggregateIdMatchesProductId() {
                var product = ProductRoot.initialize(createCommand);
                var event = product.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(product.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var product = ProductRoot.initialize(createCommand);
                var event = product.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var product = ProductRoot.initialize(createCommand);
                var event = product.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(product.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries the merchantId")
            void eventCarriesMerchantId() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getMerchantId()).isEqualTo(merchantId.value());
            }

            @Test
            @DisplayName("Event carries the categoryId")
            void eventCarriesCategoryId() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getCategoryId()).isEqualTo(categoryId.value());
            }

            @Test
            @DisplayName("Event carries the brandId")
            void eventCarriesBrandId() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getBrandId()).isEqualTo(brandId.value());
            }

            @Test
            @DisplayName("Event carries the name")
            void eventCarriesName() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(name.value());
            }

            @Test
            @DisplayName("Event carries the description")
            void eventCarriesDescription() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getDescription()).isEqualTo(description.value());
            }

            @Test
            @DisplayName("Event carries the barcode")
            void eventCarriesBarcode() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getBarcode()).isEqualTo(barcode.value());
            }

            @Test
            @DisplayName("Event carries DRAFT status")
            void eventCarriesStatus() {
                var product = ProductRoot.initialize(createCommand);
                var event = (ProductCreatedEvent) product.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.DRAFT.name());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // sentToApproval()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("sentToApproval()")
    class SentToApproval {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets status to SENT_TO_APPROVAL from DRAFT")
            void setsStatusFromDraft() {
                var result = freshProduct().sentToApproval();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            }

            @Test
            @DisplayName("Sets status to SENT_TO_APPROVAL from REJECTED")
            void setsStatusFromRejected() {
                var result = productInRejectedState().sentToApproval();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var original = freshProduct();
                var result = original.sentToApproval();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshProduct().sentToApproval();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.sentToApproval();

                assertThat(original.getStatus()).isEqualTo(ProductStatus.DRAFT);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().sentToApproval();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSentToApprovalEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().sentToApproval();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSentToApprovalEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the product ID")
            void eventAggregateIdMatchesProductId() {
                var result = freshProduct().sentToApproval();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var result = freshProduct().sentToApproval();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = freshProduct().sentToApproval();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Event carries SENT_TO_APPROVAL status")
            void eventCarriesStatus() {
                var result = freshProduct().sentToApproval();
                var event = (ProductSentToApprovalEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL.name());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is APPROVED")
            void throwsWhenApproved() {
                var approved = productInApprovedState();

                assertThatThrownBy(approved::sentToApproval)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is SENT_TO_APPROVAL")
            void throwsWhenAlreadySentToApproval() {
                var sentToApproval = productInSentToApprovalState();

                assertThatThrownBy(sentToApproval::sentToApproval)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is ARCHIVED")
            void throwsWhenArchived() {
                var archived = productInApprovedState().archive();

                assertThatThrownBy(archived::sentToApproval)
                        .isInstanceOf(ProductDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // approve()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("approve()")
    class Approve {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets status to APPROVED")
            void setsStatusToApproved() {
                var result = productInSentToApprovalState().approve();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.APPROVED);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var sentToApproval = productInSentToApprovalState();
                var result = sentToApproval.approve();

                assertThat(result.getRootID()).isEqualTo(sentToApproval.getRootID());
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = productInSentToApprovalState().approve();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var sentToApproval = productInSentToApprovalState();
                sentToApproval.approve();

                assertThat(sentToApproval.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = productInSentToApprovalState().approve();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductApprovedEvent")
            void registeredEventIsCorrectType() {
                var result = productInSentToApprovalState().approve();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductApprovedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the product ID")
            void eventAggregateIdMatchesProductId() {
                var result = productInSentToApprovalState().approve();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event carries APPROVED status")
            void eventCarriesStatus() {
                var result = productInSentToApprovalState().approve();
                var event = (ProductApprovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.APPROVED.name());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reject()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("reject()")
    class Reject {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets status to REJECTED")
            void setsStatusToRejected() {
                var result = productInSentToApprovalState().reject();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.REJECTED);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var sentToApproval = productInSentToApprovalState();
                var result = sentToApproval.reject();

                assertThat(result.getRootID()).isEqualTo(sentToApproval.getRootID());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var sentToApproval = productInSentToApprovalState();
                sentToApproval.reject();

                assertThat(sentToApproval.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = productInSentToApprovalState().reject();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductRejectedEvent")
            void registeredEventIsCorrectType() {
                var result = productInSentToApprovalState().reject();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductRejectedEvent.class);
            }

            @Test
            @DisplayName("Event carries REJECTED status")
            void eventCarriesStatus() {
                var result = productInSentToApprovalState().reject();
                var event = (ProductRejectedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.REJECTED.name());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // moveToDraft()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("moveToDraft()")
    class MoveToDraft {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets status to DRAFT from SENT_TO_APPROVAL")
            void setsStatusToDraft() {
                var result = productInSentToApprovalState().moveToDraft();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.DRAFT);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var sentToApproval = productInSentToApprovalState();
                var result = sentToApproval.moveToDraft();

                assertThat(result.getRootID()).isEqualTo(sentToApproval.getRootID());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var sentToApproval = productInSentToApprovalState();
                sentToApproval.moveToDraft();

                assertThat(sentToApproval.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = productInSentToApprovalState().moveToDraft();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductMovedToDraftEvent")
            void registeredEventIsCorrectType() {
                var result = productInSentToApprovalState().moveToDraft();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductMovedToDraftEvent.class);
            }

            @Test
            @DisplayName("Event carries DRAFT status")
            void eventCarriesStatus() {
                var result = productInSentToApprovalState().moveToDraft();
                var event = (ProductMovedToDraftEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.DRAFT.name());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is DRAFT")
            void throwsWhenDraft() {
                var draft = freshProduct();

                assertThatThrownBy(draft::moveToDraft)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is APPROVED")
            void throwsWhenApproved() {
                var approved = productInApprovedState();

                assertThatThrownBy(approved::moveToDraft)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is REJECTED")
            void throwsWhenRejected() {
                var rejected = productInRejectedState();

                assertThatThrownBy(rejected::moveToDraft)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is ARCHIVED")
            void throwsWhenArchived() {
                var archived = productInApprovedState().archive();

                assertThatThrownBy(archived::moveToDraft)
                        .isInstanceOf(ProductDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // archive()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("archive()")
    class Archive {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Sets status to ARCHIVED from APPROVED")
            void setsStatusToArchived() {
                var result = productInApprovedState().archive();

                assertThat(result.getStatus()).isEqualTo(ProductStatus.ARCHIVED);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var approved = productInApprovedState();
                var result = approved.archive();

                assertThat(result.getRootID()).isEqualTo(approved.getRootID());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var approved = productInApprovedState();
                approved.archive();

                assertThat(approved.getStatus()).isEqualTo(ProductStatus.APPROVED);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = productInApprovedState().archive();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductArchivedEvent")
            void registeredEventIsCorrectType() {
                var result = productInApprovedState().archive();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductArchivedEvent.class);
            }

            @Test
            @DisplayName("Event carries ARCHIVED status")
            void eventCarriesStatus() {
                var result = productInApprovedState().archive();
                var event = (ProductArchivedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(ProductStatus.ARCHIVED.name());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is DRAFT")
            void throwsWhenDraft() {
                var draft = freshProduct();

                assertThatThrownBy(draft::archive)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is SENT_TO_APPROVAL")
            void throwsWhenSentToApproval() {
                var sentToApproval = productInSentToApprovalState();

                assertThatThrownBy(sentToApproval::archive)
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Throws when status is REJECTED")
            void throwsWhenRejected() {
                var rejected = productInRejectedState();

                assertThatThrownBy(rejected::archive)
                        .isInstanceOf(ProductDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private ProductName newName;
        private ProductDescription newDescription;
        private ProductChangeInformationCommand changeCommand;

        @BeforeEach
        void setUpCommand() {
            newName = new ProductName("Office Laptop");
            newDescription = new ProductDescription("A lightweight laptop for office work");
            changeCommand = ProductChangeInformationCommand.builder()
                    .productId(ProductId.random())
                    .name(newName)
                    .description(newDescription)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates name and description")
            void updatesNameAndDescription() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getDescription()).isEqualTo(newDescription);
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var original = freshProduct();
                var result = original.changeInformation(changeCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("categoryId is preserved")
            void categoryIdPreserved() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getCategoryId()).isEqualTo(categoryId);
            }

            @Test
            @DisplayName("brandId is preserved")
            void brandIdPreserved() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getBrandId()).isEqualTo(brandId);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.changeInformation(changeCommand);

                assertThat(original.getName()).isEqualTo(name);
                assertThat(original.getDescription()).isEqualTo(description);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductInformationChangedEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductInformationChangedEvent.class);
            }

            @Test
            @DisplayName("Event carries the new name and description")
            void eventCarriesNewNameAndDescription() {
                var result = freshProduct().changeInformation(changeCommand);
                var event = (ProductInformationChangedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getName()).isEqualTo(newName.value());
                assertThat(event.getDescription()).isEqualTo(newDescription.value());
            }
        }

        @Nested
        @DisplayName("Guard")
        class Guard {

            @Test
            @DisplayName("Throws when status is SENT_TO_APPROVAL")
            void throwsWhenSentToApproval() {
                var sentToApproval = productInSentToApprovalState();

                assertThatThrownBy(() -> sentToApproval.changeInformation(changeCommand))
                        .isInstanceOf(ProductDomainException.class);
            }

            @Test
            @DisplayName("Allowed when status is DRAFT")
            void allowedWhenDraft() {
                var result = freshProduct().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
            }

            @Test
            @DisplayName("Allowed when status is APPROVED")
            void allowedWhenApproved() {
                var result = productInApprovedState().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
            }

            @Test
            @DisplayName("Allowed when status is REJECTED")
            void allowedWhenRejected() {
                var result = productInRejectedState().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // assignCategory()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignCategory()")
    class AssignCategory {

        private ProductCategoryId newCategoryId;
        private ProductAssignCategoryCommand assignCategoryCommand;

        @BeforeEach
        void setUpCommand() {
            newCategoryId = ProductCategoryId.random();
            assignCategoryCommand = ProductAssignCategoryCommand.builder()
                    .productId(ProductId.random())
                    .categoryId(newCategoryId)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates categoryId")
            void updatesCategoryId() {
                var result = freshProduct().assignCategory(assignCategoryCommand);

                assertThat(result.getCategoryId()).isEqualTo(newCategoryId);
                assertThat(result.getCategoryId().value()).isEqualTo(newCategoryId.value());
            }

            @Test
            @DisplayName("ID is preserved")
            void idPreserved() {
                var original = freshProduct();
                var result = original.assignCategory(assignCategoryCommand);

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.assignCategory(assignCategoryCommand);

                assertThat(original.getCategoryId()).isEqualTo(categoryId);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().assignCategory(assignCategoryCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductCategoryAssignedEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().assignCategory(assignCategoryCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductCategoryAssignedEvent.class);
            }

            @Test
            @DisplayName("Event carries the new categoryId")
            void eventCarriesCategoryId() {
                var result = freshProduct().assignCategory(assignCategoryCommand);
                var event = (ProductCategoryAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getCategoryId()).isEqualTo(newCategoryId.value());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // assignBrand()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignBrand()")
    class AssignBrand {

        private BrandId newBrandId;
        private ProductAssignBrandCommand assignBrandCommand;

        @BeforeEach
        void setUpCommand() {
            newBrandId = BrandId.random();
            assignBrandCommand = ProductAssignBrandCommand.builder()
                    .productId(ProductId.random())
                    .brandId(newBrandId)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Updates brandId")
            void updatesBrandId() {
                var result = freshProduct().assignBrand(assignBrandCommand);

                assertThat(result.getBrandId()).isEqualTo(newBrandId);
                assertThat(result.getBrandId().value()).isEqualTo(newBrandId.value());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.assignBrand(assignBrandCommand);

                assertThat(original.getBrandId()).isEqualTo(brandId);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().assignBrand(assignBrandCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductBrandAssignedEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().assignBrand(assignBrandCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductBrandAssignedEvent.class);
            }

            @Test
            @DisplayName("Event carries the new brandId")
            void eventCarriesBrandId() {
                var result = freshProduct().assignBrand(assignBrandCommand);
                var event = (ProductBrandAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getBrandId()).isEqualTo(newBrandId.value());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // assignSpecification()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("assignSpecification()")
    class AssignSpecification {

        private ProductSpecificationId specId;
        private ProductSpecificationValue specValue;
        private ProductAssignSpecificationCommand assignSpecCommand;

        @BeforeEach
        void setUpCommand() {
            specId = ProductSpecificationId.random();
            specValue = new ProductSpecificationValue("Black");
            assignSpecCommand = ProductAssignSpecificationCommand.builder()
                    .productId(ProductId.random())
                    .specificationId(specId)
                    .value(specValue)
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds specification to empty list")
            void addsToEmptyList() {
                var result = freshProduct().assignSpecification(assignSpecCommand);

                assertThat(result.getSpecifications()).hasSize(1);
                assertThat(result.getSpecifications().getFirst().getSpecificationId()).isEqualTo(specId);
                assertThat(result.getSpecifications().getFirst().getValue()).isEqualTo(specValue);
            }

            @Test
            @DisplayName("Upserts — same specificationId replaces existing value")
            void upsertsExistingSpecification() {
                var updatedValue = new ProductSpecificationValue("White");
                var updateCommand = ProductAssignSpecificationCommand.builder()
                        .productId(ProductId.random())
                        .specificationId(specId)
                        .value(updatedValue)
                        .build();

                var afterFirst = freshProduct().assignSpecification(assignSpecCommand);
                var afterSecond = afterFirst.assignSpecification(updateCommand);

                assertThat(afterSecond.getSpecifications()).hasSize(1);
                assertThat(afterSecond.getSpecifications().getFirst().getValue()).isEqualTo(updatedValue);
            }

            @Test
            @DisplayName("Different specificationIds produce independent entries")
            void differentIdsProduceIndependentEntries() {
                var otherId = ProductSpecificationId.random();
                var otherCommand = ProductAssignSpecificationCommand.builder()
                        .productId(ProductId.random())
                        .specificationId(otherId)
                        .value(new ProductSpecificationValue("Large"))
                        .build();

                var result = freshProduct()
                        .assignSpecification(assignSpecCommand)
                        .assignSpecification(otherCommand);

                assertThat(result.getSpecifications()).hasSize(2);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.assignSpecification(assignSpecCommand);

                assertThat(original.getSpecifications()).isEmpty();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().assignSpecification(assignSpecCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationAssignedEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().assignSpecification(assignSpecCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationAssignedEvent.class);
            }

            @Test
            @DisplayName("Event carries specificationId and value")
            void eventCarriesSpecificationIdAndValue() {
                var result = freshProduct().assignSpecification(assignSpecCommand);
                var event = (ProductSpecificationAssignedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getSpecificationId()).isEqualTo(specId.value());
                assertThat(event.getValue()).isEqualTo(specValue.value());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // removeSpecification()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("removeSpecification()")
    class RemoveSpecification {

        private ProductSpecificationId specId;
        private ProductRemoveSpecificationCommand removeSpecCommand;

        @BeforeEach
        void setUpCommand() {
            specId = ProductSpecificationId.random();
            removeSpecCommand = ProductRemoveSpecificationCommand.builder()
                    .productId(ProductId.random())
                    .specificationId(specId)
                    .build();
        }

        private ProductRoot productWithSpec() {
            var addCmd = ProductAssignSpecificationCommand.builder()
                    .productId(ProductId.random())
                    .specificationId(specId)
                    .value(new ProductSpecificationValue("Black"))
                    .build();
            return freshProduct().assignSpecification(addCmd);
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Removes specification by ID")
            void removesSpecification() {
                var result = productWithSpec().removeSpecification(removeSpecCommand);

                assertThat(result.getSpecifications()).isEmpty();
            }

            @Test
            @DisplayName("Other specifications are preserved")
            void otherSpecificationsPreserved() {
                var otherId = ProductSpecificationId.random();
                var otherCmd = ProductAssignSpecificationCommand.builder()
                        .productId(ProductId.random())
                        .specificationId(otherId)
                        .value(new ProductSpecificationValue("Large"))
                        .build();

                var withTwo = productWithSpec().assignSpecification(otherCmd);
                var result = withTwo.removeSpecification(removeSpecCommand);

                assertThat(result.getSpecifications()).hasSize(1);
                assertThat(result.getSpecifications().getFirst().getSpecificationId()).isEqualTo(otherId);
            }

            @Test
            @DisplayName("No-op when specificationId not found")
            void noOpWhenNotFound() {
                var result = freshProduct().removeSpecification(removeSpecCommand);

                assertThat(result.getSpecifications()).isEmpty();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = productWithSpec();
                original.removeSpecification(removeSpecCommand);

                assertThat(original.getSpecifications()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = productWithSpec().removeSpecification(removeSpecCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductSpecificationRemovedEvent")
            void registeredEventIsCorrectType() {
                var result = productWithSpec().removeSpecification(removeSpecCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductSpecificationRemovedEvent.class);
            }

            @Test
            @DisplayName("Event carries the specificationId")
            void eventCarriesSpecificationId() {
                var result = productWithSpec().removeSpecification(removeSpecCommand);
                var event = (ProductSpecificationRemovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getSpecificationId()).isEqualTo(specId.value());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // addVariant()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("addVariant()")
    class AddVariant {

        private VariantKeyId keyId;
        private VariantValueId valueId;
        private ProductAddVariantCommand addVariantCommand;

        @BeforeEach
        void setUpCommand() {
            keyId = VariantKeyId.random();
            valueId = VariantValueId.random();
            addVariantCommand = ProductAddVariantCommand.builder()
                    .productId(ProductId.random())
                    .assignments(List.of(ProductVariantAssignment.of(keyId, valueId)))
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Adds variant to empty list")
            void addsVariantToEmptyList() {
                var result = freshProduct().addVariant(addVariantCommand);

                assertThat(result.getVariants()).hasSize(1);
            }

            @Test
            @DisplayName("Added variant has a non-null ID")
            void addedVariantHasNonNullId() {
                var result = freshProduct().addVariant(addVariantCommand);

                assertThat(result.getVariants().getFirst().getRootID()).isNotNull();
                assertThat(result.getVariants().getFirst().getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Added variant has correct assignments")
            void addedVariantHasCorrectAssignments() {
                var result = freshProduct().addVariant(addVariantCommand);
                var variant = result.getVariants().getFirst();

                assertThat(variant.getAssignments()).hasSize(1);
                assertThat(variant.getAssignments().getFirst().getVariantKeyId()).isEqualTo(keyId);
                assertThat(variant.getAssignments().getFirst().getVariantValueId()).isEqualTo(valueId);
            }

            @Test
            @DisplayName("Adding two variants results in two entries")
            void addingTwoVariantsResultsInTwoEntries() {
                var secondCmd = ProductAddVariantCommand.builder()
                        .productId(ProductId.random())
                        .assignments(List.of(ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random())))
                        .build();

                var result = freshProduct()
                        .addVariant(addVariantCommand)
                        .addVariant(secondCmd);

                assertThat(result.getVariants()).hasSize(2);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = freshProduct();
                original.addVariant(addVariantCommand);

                assertThat(original.getVariants()).isEmpty();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = freshProduct().addVariant(addVariantCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantAddedEvent")
            void registeredEventIsCorrectType() {
                var result = freshProduct().addVariant(addVariantCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantAddedEvent.class);
            }

            @Test
            @DisplayName("Event variantId matches the created variant's ID")
            void eventVariantIdMatchesCreatedVariant() {
                var result = freshProduct().addVariant(addVariantCommand);
                var event = (ProductVariantAddedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getVariantId())
                        .isEqualTo(result.getVariants().getFirst().getRootID().value());
            }

            @Test
            @DisplayName("Event carries the variantKeyIds and variantValueIds")
            void eventCarriesAssignmentIds() {
                var result = freshProduct().addVariant(addVariantCommand);
                var event = (ProductVariantAddedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getVariantKeyIds()).containsExactly(keyId.value());
                assertThat(event.getVariantValueIds()).containsExactly(valueId.value());
            }

        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // removeVariant()
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("removeVariant()")
    class RemoveVariant {

        private ProductAddVariantCommand addVariantCommand;

        @BeforeEach
        void setUpCommand() {
            addVariantCommand = ProductAddVariantCommand.builder()
                    .productId(ProductId.random())
                    .assignments(List.of(ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random())))
                    .build();
        }

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Removes variant by ID")
            void removesVariantById() {
                var withVariant = freshProduct().addVariant(addVariantCommand);
                var variantId = withVariant.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) variantId)
                        .build();

                var result = withVariant.removeVariant(removeCmd);

                assertThat(result.getVariants()).isEmpty();
            }

            @Test
            @DisplayName("Other variants are preserved")
            void otherVariantsPreserved() {
                var secondCmd = ProductAddVariantCommand.builder()
                        .productId(ProductId.random())
                        .assignments(List.of(ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random())))
                        .build();

                var withTwo = freshProduct().addVariant(addVariantCommand).addVariant(secondCmd);
                var firstVariantId = withTwo.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) firstVariantId)
                        .build();

                var result = withTwo.removeVariant(removeCmd);

                assertThat(result.getVariants()).hasSize(1);
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var withVariant = freshProduct().addVariant(addVariantCommand);
                var variantId = withVariant.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) variantId)
                        .build();

                withVariant.removeVariant(removeCmd);

                assertThat(withVariant.getVariants()).hasSize(1);
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var withVariant = freshProduct().addVariant(addVariantCommand);
                var variantId = withVariant.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) variantId)
                        .build();

                var result = withVariant.removeVariant(removeCmd);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is ProductVariantRemovedEvent")
            void registeredEventIsCorrectType() {
                var withVariant = freshProduct().addVariant(addVariantCommand);
                var variantId = withVariant.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) variantId)
                        .build();

                var result = withVariant.removeVariant(removeCmd);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(ProductVariantRemovedEvent.class);
            }

            @Test
            @DisplayName("Event carries the removed variantId")
            void eventCarriesVariantId() {
                var withVariant = freshProduct().addVariant(addVariantCommand);
                var variantId = withVariant.getVariants().getFirst().getRootID();

                var removeCmd = ProductRemoveVariantCommand.builder()
                        .productId(ProductId.random())
                        .variantId((ProductVariantId) variantId)
                        .build();

                var result = withVariant.removeVariant(removeCmd);
                var event = (ProductVariantRemovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getVariantId()).isEqualTo(variantId.value());
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
            assertThat(freshProduct().getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Specifications list is unmodifiable after assignSpecification")
        void specificationsListIsUnmodifiable() {
            var specId = ProductSpecificationId.random();
            var cmd = ProductAssignSpecificationCommand.builder()
                    .productId(ProductId.random())
                    .specificationId(specId)
                    .value(new ProductSpecificationValue("Black"))
                    .build();

            assertThat(freshProduct().assignSpecification(cmd).getSpecifications()).isUnmodifiable();
        }

        @Test
        @DisplayName("Variants list is unmodifiable after addVariant")
        void variantsListIsUnmodifiable() {
            var cmd = ProductAddVariantCommand.builder()
                    .productId(ProductId.random())
                    .assignments(List.of(ProductVariantAssignment.of(VariantKeyId.random(), VariantValueId.random())))
                    .build();

            assertThat(freshProduct().addVariant(cmd).getVariants()).isUnmodifiable();
        }

        @Test
        @DisplayName("Sequential lifecycle operations return new instances each time")
        void sequentialLifecycleOperationsReturnNewInstances() {
            var draft = freshProduct();
            var sentToApproval = draft.sentToApproval();
            var approved = sentToApproval.approve();

            assertThat(draft.getStatus()).isEqualTo(ProductStatus.DRAFT);
            assertThat(sentToApproval.getStatus()).isEqualTo(ProductStatus.SENT_TO_APPROVAL);
            assertThat(approved.getStatus()).isEqualTo(ProductStatus.APPROVED);
        }

        @Test
        @DisplayName("Two different create commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherCommand = ProductCreateCommand.builder()
                    .merchantId(MerchantId.random())
                    .categoryId(ProductCategoryId.random())
                    .brandId(BrandId.random())
                    .name(new ProductName("Tablet Model"))
                    .description(new ProductDescription("A portable tablet for everyday use"))
                    .barcode(Barcode.of("9876543210987"))
                    .build();

            var first = freshProduct();
            var second = ProductRoot.initialize(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getMerchantId()).isNotEqualTo(second.getMerchantId());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }
    }
}