package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.event.brand.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BrandRoot")
class BrandRootTest {

    private MerchantId owner;
    private BrandName name;
    private BrandDescription description;
    private BrandPath path;
    private BrandCreateCommand createCommand;

    @BeforeEach
    void setUp() {
        owner = MerchantId.from(UUID.randomUUID());
        name = new BrandName("Nike Brand");
        description = new BrandDescription("A well-known global sports brand");
        path = new BrandPath("nike-brand");
        createCommand = BrandCreateCommand.builder()
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .build();
    }

    private BrandRoot brandInDraftState() {
        return BrandRoot.builder()
                .id(BrandId.random())
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.DRAFT)
                .build();
    }

    private BrandRoot brandInSentToApprovalState() {
        return BrandRoot.builder()
                .id(BrandId.random())
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.SENT_TO_APPROVAL)
                .build();
    }

    private BrandRoot brandInApprovedState() {
        return BrandRoot.builder()
                .id(BrandId.random())
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.APPROVED)
                .build();
    }

    private BrandRoot brandInRejectedState() {
        return BrandRoot.builder()
                .id(BrandId.random())
                .owner(owner)
                .name(name)
                .description(description)
                .path(path)
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.REJECTED)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initializeForMerchant
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("initializeForMerchant()")
    class InitializeForMerchant {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getRootID()).isNotNull();
                assertThat(brand.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = BrandRoot.initializeForMerchant(createCommand);
                var second = BrandRoot.initializeForMerchant(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets owner from command")
            void setsOwnerFromCommand() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getOwner()).isEqualTo(owner);
                assertThat(brand.getOwner().value()).isEqualTo(owner.value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getName()).isEqualTo(name);
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescriptionFromCommand() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Sets path from command")
            void setsPathFromCommand() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getPath()).isEqualTo(path);
            }

            @Test
            @DisplayName("isGlobal is false")
            void isGlobalIsFalse() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getIsGlobal()).isFalse();
            }

            @Test
            @DisplayName("Status is DRAFT")
            void statusIsDraft() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandCreatedEvent")
            void registeredEventIsBrandCreatedEvent() {
                var brand = BrandRoot.initializeForMerchant(createCommand);

                assertThat(brand.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandCreatedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var brand = BrandRoot.initializeForMerchant(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(brand.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var brand = BrandRoot.initializeForMerchant(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var brand = BrandRoot.initializeForMerchant(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(brand.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // initializeGlobal
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("initializeGlobal()")
    class InitializeGlobal {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Assigns a non-null ID")
            void assignsNonNullId() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getRootID()).isNotNull();
                assertThat(brand.getRootID().value()).isNotNull();
            }

            @Test
            @DisplayName("Each call generates a unique ID")
            void eachCallGeneratesUniqueId() {
                var first = BrandRoot.initializeGlobal(createCommand);
                var second = BrandRoot.initializeGlobal(createCommand);

                assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            }

            @Test
            @DisplayName("Sets owner from command")
            void setsOwnerFromCommand() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getOwner()).isEqualTo(owner);
                assertThat(brand.getOwner().value()).isEqualTo(owner.value());
            }

            @Test
            @DisplayName("Sets name from command")
            void setsNameFromCommand() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getName()).isEqualTo(name);
            }

            @Test
            @DisplayName("Sets description from command")
            void setsDescriptionFromCommand() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getDescription()).isEqualTo(description);
            }

            @Test
            @DisplayName("Sets path from command")
            void setsPathFromCommand() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getPath()).isEqualTo(path);
            }

            @Test
            @DisplayName("isGlobal is true")
            void isGlobalIsTrue() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getIsGlobal()).isTrue();
            }

            @Test
            @DisplayName("Status is APPROVED")
            void statusIsApproved() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getStatus()).isEqualTo(BrandStatus.APPROVED);
            }

            @Test
            @DisplayName("Sets a non-null modificationTs")
            void setsModificationTs() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getModificationTs()).isNotNull();
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandCreatedGlobalEvent")
            void registeredEventIsBrandCreatedGlobalEvent() {
                var brand = BrandRoot.initializeGlobal(createCommand);

                assertThat(brand.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandCreatedGlobalEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var brand = BrandRoot.initializeGlobal(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(brand.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event has a non-null eventId")
            void eventHasNonNullEventId() {
                var brand = BrandRoot.initializeGlobal(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getEventId()).isNotNull();
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var brand = BrandRoot.initializeGlobal(createCommand);
                var event = brand.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(brand.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // approve
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("approve()")
    class Approve {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status is APPROVED")
            void statusIsApproved() {
                var result = brandInDraftState().approve();

                assertThat(result.getStatus()).isEqualTo(BrandStatus.APPROVED);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var original = brandInDraftState();
                var result = original.approve();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInDraftState();
                original.approve();

                assertThat(original.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }

            @Test
            @DisplayName("Other fields are preserved after approve")
            void otherFieldsPreserved() {
                var original = brandInDraftState();
                var result = original.approve();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getOwner()).isEqualTo(original.getOwner());
                assertThat(result.getName()).isEqualTo(original.getName());
                assertThat(result.getDescription()).isEqualTo(original.getDescription());
                assertThat(result.getPath()).isEqualTo(original.getPath());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInDraftState().approve();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandApprovedEvent")
            void registeredEventIsBrandApprovedEvent() {
                var result = brandInDraftState().approve();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandApprovedEvent.class);
            }

            @Test
            @DisplayName("Event status is APPROVED")
            void eventStatusIsApproved() {
                var result = brandInDraftState().approve();
                var event = (BrandApprovedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(BrandStatus.APPROVED.name());
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInDraftState().approve();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = brandInDraftState().approve();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // reject
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("reject()")
    class Reject {

        @Nested
        @DisplayName("Aggregate state")
        class AggregateState {

            @Test
            @DisplayName("Status is REJECTED")
            void statusIsRejected() {
                var result = brandInDraftState().reject();

                assertThat(result.getStatus()).isEqualTo(BrandStatus.REJECTED);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = brandInDraftState().reject();

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInDraftState();
                original.reject();

                assertThat(original.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }

            @Test
            @DisplayName("Other fields are preserved after reject")
            void otherFieldsPreserved() {
                var original = brandInDraftState();
                var result = original.reject();

                assertThat(result.getRootID()).isEqualTo(original.getRootID());
                assertThat(result.getOwner()).isEqualTo(original.getOwner());
                assertThat(result.getName()).isEqualTo(original.getName());
            }
        }

        @Nested
        @DisplayName("Event publishing")
        class EventPublishing {

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInDraftState().reject();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandRejectedEvent")
            void registeredEventIsBrandRejectedEvent() {
                var result = brandInDraftState().reject();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandRejectedEvent.class);
            }

            @Test
            @DisplayName("Event status is REJECTED")
            void eventStatusIsRejected() {
                var result = brandInDraftState().reject();
                var event = (BrandRejectedEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(BrandStatus.REJECTED.name());
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInDraftState().reject();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = brandInDraftState().reject();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // sentToApproval
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("sentToApproval()")
    class SentToApproval {

        @Nested
        @DisplayName("When status is DRAFT")
        class WhenStatusIsDraft {

            @Test
            @DisplayName("Status becomes SENT_TO_APPROVAL")
            void statusIsSentToApproval() {
                var result = brandInDraftState().sentToApproval();

                assertThat(result.getStatus()).isEqualTo(BrandStatus.SENT_TO_APPROVAL);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInDraftState().sentToApproval();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandSentToApprovalEvent")
            void registeredEventIsBrandSentToApprovalEvent() {
                var result = brandInDraftState().sentToApproval();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandSentToApprovalEvent.class);
            }

            @Test
            @DisplayName("Event status is SENT_TO_APPROVAL")
            void eventStatusIsSentToApproval() {
                var result = brandInDraftState().sentToApproval();
                var event = (BrandSentToApprovalEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(BrandStatus.SENT_TO_APPROVAL.name());
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInDraftState().sentToApproval();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInDraftState();
                original.sentToApproval();

                assertThat(original.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }
        }

        @Nested
        @DisplayName("When status is REJECTED")
        class WhenStatusIsRejected {

            @Test
            @DisplayName("Status becomes SENT_TO_APPROVAL")
            void statusIsSentToApproval() {
                var result = brandInRejectedState().sentToApproval();

                assertThat(result.getStatus()).isEqualTo(BrandStatus.SENT_TO_APPROVAL);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInRejectedState().sentToApproval();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandSentToApprovalEvent")
            void registeredEventIsBrandSentToApprovalEvent() {
                var result = brandInRejectedState().sentToApproval();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandSentToApprovalEvent.class);
            }
        }

        @Nested
        @DisplayName("Guard: invalid statuses")
        class Guard {

            @Test
            @DisplayName("Throws BrandDomainException when status is SENT_TO_APPROVAL")
            void throwsWhenStatusIsSentToApproval() {
                assertThatThrownBy(() -> brandInSentToApprovalState().sentToApproval())
                        .isInstanceOf(BrandDomainException.class);
            }

            @Test
            @DisplayName("Throws BrandDomainException when status is APPROVED")
            void throwsWhenStatusIsApproved() {
                assertThatThrownBy(() -> brandInApprovedState().sentToApproval())
                        .isInstanceOf(BrandDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // moveToDraft
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("moveToDraft()")
    class MoveToDraft {

        @Nested
        @DisplayName("When status is SENT_TO_APPROVAL")
        class WhenStatusIsSentToApproval {

            @Test
            @DisplayName("Status becomes DRAFT")
            void statusIsDraft() {
                var result = brandInSentToApprovalState().moveToDraft();

                assertThat(result.getStatus()).isEqualTo(BrandStatus.DRAFT);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInSentToApprovalState().moveToDraft();

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandMovedToDraftEvent")
            void registeredEventIsBrandMovedToDraftEvent() {
                var result = brandInSentToApprovalState().moveToDraft();

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandMovedToDraftEvent.class);
            }

            @Test
            @DisplayName("Event status is DRAFT")
            void eventStatusIsDraft() {
                var result = brandInSentToApprovalState().moveToDraft();
                var event = (BrandMovedToDraftEvent) result.getUncommittedEvents().getFirst();

                assertThat(event.getStatus()).isEqualTo(BrandStatus.DRAFT.name());
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInSentToApprovalState().moveToDraft();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = brandInSentToApprovalState().moveToDraft();
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInSentToApprovalState();
                original.moveToDraft();

                assertThat(original.getStatus()).isEqualTo(BrandStatus.SENT_TO_APPROVAL);
            }
        }

        @Nested
        @DisplayName("Guard: invalid statuses")
        class Guard {

            @Test
            @DisplayName("Throws BrandDomainException when status is DRAFT")
            void throwsWhenStatusIsDraft() {
                assertThatThrownBy(() -> brandInDraftState().moveToDraft())
                        .isInstanceOf(BrandDomainException.class);
            }

            @Test
            @DisplayName("Throws BrandDomainException when status is APPROVED")
            void throwsWhenStatusIsApproved() {
                assertThatThrownBy(() -> brandInApprovedState().moveToDraft())
                        .isInstanceOf(BrandDomainException.class);
            }

            @Test
            @DisplayName("Throws BrandDomainException when status is REJECTED")
            void throwsWhenStatusIsRejected() {
                assertThatThrownBy(() -> brandInRejectedState().moveToDraft())
                        .isInstanceOf(BrandDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeInformation
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeInformation()")
    class ChangeInformation {

        private BrandName newName;
        private BrandDescription newDescription;
        private BrandPath newPath;
        private BrandChangeInformationCommand changeCommand;

        @BeforeEach
        void setUpChangeCommand() {
            newName = new BrandName("Adidas Brand");
            newDescription = new BrandDescription("A well-known German sports brand");
            newPath = new BrandPath("adidas-brand");
            changeCommand = BrandChangeInformationCommand.builder()
                    .brandId(BrandId.random())
                    .name(newName)
                    .description(newDescription)
                    .path(newPath)
                    .build();
        }

        @Nested
        @DisplayName("When status is not SENT_TO_APPROVAL")
        class WhenStatusIsNotSentToApproval {

            @Test
            @DisplayName("Updates name from command")
            void updatesName() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
            }

            @Test
            @DisplayName("Updates description from command")
            void updatesDescription() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getDescription()).isEqualTo(newDescription);
            }

            @Test
            @DisplayName("Updates path from command")
            void updatesPath() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getPath()).isEqualTo(newPath);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInDraftState();
                original.changeInformation(changeCommand);

                assertThat(original.getName()).isEqualTo(name);
                assertThat(original.getDescription()).isEqualTo(description);
                assertThat(original.getPath()).isEqualTo(path);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandInformationChangedEvent")
            void registeredEventIsBrandInformationChangedEvent() {
                var result = brandInDraftState().changeInformation(changeCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandInformationChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInDraftState().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = brandInDraftState().changeInformation(changeCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Works when status is APPROVED")
            void worksWhenApproved() {
                var result = brandInApprovedState().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getStatus()).isEqualTo(BrandStatus.APPROVED);
            }

            @Test
            @DisplayName("Works when status is REJECTED")
            void worksWhenRejected() {
                var result = brandInRejectedState().changeInformation(changeCommand);

                assertThat(result.getName()).isEqualTo(newName);
                assertThat(result.getStatus()).isEqualTo(BrandStatus.REJECTED);
            }
        }

        @Nested
        @DisplayName("Guard: SENT_TO_APPROVAL status")
        class Guard {

            @Test
            @DisplayName("Throws BrandDomainException when status is SENT_TO_APPROVAL")
            void throwsWhenStatusIsSentToApproval() {
                assertThatThrownBy(() -> brandInSentToApprovalState().changeInformation(changeCommand))
                        .isInstanceOf(BrandDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeImage
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeImage()")
    class ChangeImage {

        private String newImage;
        private BrandChangeImageCommand imageCommand;

        @BeforeEach
        void setUpImageCommand() {
            newImage = "https://cdn.example.com/brands/nike-logo.png";
            imageCommand = BrandChangeImageCommand.builder()
                    .brandId(BrandId.random())
                    .image(newImage)
                    .build();
        }

        @Nested
        @DisplayName("When status is not SENT_TO_APPROVAL")
        class WhenStatusIsNotSentToApproval {

            @Test
            @DisplayName("Updates image from command")
            void updatesImage() {
                var result = brandInDraftState().changeImage(imageCommand);

                assertThat(result.getImage()).isEqualTo(newImage);
            }

            @Test
            @DisplayName("modificationTs is updated")
            void modificationTsIsUpdated() {
                var result = brandInDraftState().changeImage(imageCommand);

                assertThat(result.getModificationTs()).isNotNull();
            }

            @Test
            @DisplayName("Original aggregate is unchanged")
            void originalAggregateIsUnchanged() {
                var original = brandInDraftState();
                var originalImage = original.getImage();
                original.changeImage(imageCommand);

                assertThat(original.getImage()).isEqualTo(originalImage);
            }

            @Test
            @DisplayName("Registers exactly one uncommitted event")
            void registersExactlyOneEvent() {
                var result = brandInDraftState().changeImage(imageCommand);

                assertThat(result.getUncommittedEvents()).hasSize(1);
            }

            @Test
            @DisplayName("Registered event is BrandImageChangedEvent")
            void registeredEventIsBrandImageChangedEvent() {
                var result = brandInDraftState().changeImage(imageCommand);

                assertThat(result.getUncommittedEvents().getFirst())
                        .isInstanceOf(BrandImageChangedEvent.class);
            }

            @Test
            @DisplayName("Event aggregateId matches the brand ID")
            void eventAggregateIdMatchesBrandId() {
                var result = brandInDraftState().changeImage(imageCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getAggregateId())
                        .isEqualTo(result.getRootID().value().toString());
            }

            @Test
            @DisplayName("Event timestamp matches aggregate modificationTs")
            void eventTimestampMatchesModificationTs() {
                var result = brandInDraftState().changeImage(imageCommand);
                var event = result.getUncommittedEvents().getFirst();

                assertThat(event.getTimestamp())
                        .isEqualTo(result.getModificationTs().toOffsetDateTime());
            }

            @Test
            @DisplayName("Works when status is APPROVED")
            void worksWhenApproved() {
                var result = brandInApprovedState().changeImage(imageCommand);

                assertThat(result.getImage()).isEqualTo(newImage);
            }

            @Test
            @DisplayName("Works when status is REJECTED")
            void worksWhenRejected() {
                var result = brandInRejectedState().changeImage(imageCommand);

                assertThat(result.getImage()).isEqualTo(newImage);
            }
        }

        @Nested
        @DisplayName("Guard: SENT_TO_APPROVAL status")
        class Guard {

            @Test
            @DisplayName("Throws BrandDomainException when status is SENT_TO_APPROVAL")
            void throwsWhenStatusIsSentToApproval() {
                assertThatThrownBy(() -> brandInSentToApprovalState().changeImage(imageCommand))
                        .isInstanceOf(BrandDomainException.class);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // changeOwner
    // ─────────────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("changeOwner()")
    class ChangeOwner {

        private MerchantId newOwner;
        private BrandChangeOwnerCommand ownerCommand;

        @BeforeEach
        void setUpOwnerCommand() {
            newOwner = MerchantId.from(UUID.randomUUID());
            ownerCommand = BrandChangeOwnerCommand.builder()
                    .brandId(BrandId.random())
                    .owner(newOwner)
                    .build();
        }

        @Test
        @DisplayName("Updates owner from command")
        void updatesOwner() {
            var result = brandInDraftState().changeOwner(ownerCommand);

            assertThat(result.getOwner()).isEqualTo(newOwner);
            assertThat(result.getOwner().value()).isEqualTo(newOwner.value());
        }

        @Test
        @DisplayName("modificationTs is updated")
        void modificationTsIsUpdated() {
            var result = brandInDraftState().changeOwner(ownerCommand);

            assertThat(result.getModificationTs()).isNotNull();
        }

        @Test
        @DisplayName("Original aggregate is unchanged")
        void originalAggregateIsUnchanged() {
            var original = brandInDraftState();
            original.changeOwner(ownerCommand);

            assertThat(original.getOwner()).isEqualTo(owner);
        }

        @Test
        @DisplayName("Other fields are preserved after owner change")
        void otherFieldsPreserved() {
            var original = brandInDraftState();
            var result = original.changeOwner(ownerCommand);

            assertThat(result.getRootID()).isEqualTo(original.getRootID());
            assertThat(result.getName()).isEqualTo(original.getName());
            assertThat(result.getStatus()).isEqualTo(original.getStatus());
        }

        @Test
        @DisplayName("Registers exactly one uncommitted event")
        void registersExactlyOneEvent() {
            var result = brandInDraftState().changeOwner(ownerCommand);

            assertThat(result.getUncommittedEvents()).hasSize(1);
        }

        @Test
        @DisplayName("Registered event is BrandOwnerChangedEvent")
        void registeredEventIsBrandOwnerChangedEvent() {
            var result = brandInDraftState().changeOwner(ownerCommand);

            assertThat(result.getUncommittedEvents().getFirst())
                    .isInstanceOf(BrandOwnerChangedEvent.class);
        }

        @Test
        @DisplayName("Event owner matches the new owner")
        void eventOwnerMatchesNewOwner() {
            var result = brandInDraftState().changeOwner(ownerCommand);
            var event = (BrandOwnerChangedEvent) result.getUncommittedEvents().getFirst();

            assertThat(event.getOwner()).isEqualTo(newOwner.value());
        }

        @Test
        @DisplayName("Event aggregateId matches the brand ID")
        void eventAggregateIdMatchesBrandId() {
            var result = brandInDraftState().changeOwner(ownerCommand);
            var event = result.getUncommittedEvents().getFirst();

            assertThat(event.getAggregateId())
                    .isEqualTo(result.getRootID().value().toString());
        }

        @Test
        @DisplayName("Event timestamp matches aggregate modificationTs")
        void eventTimestampMatchesModificationTs() {
            var result = brandInDraftState().changeOwner(ownerCommand);
            var event = result.getUncommittedEvents().getFirst();

            assertThat(event.getTimestamp())
                    .isEqualTo(result.getModificationTs().toOffsetDateTime());
        }

        @Test
        @DisplayName("Works regardless of status")
        void worksRegardlessOfStatus() {
            assertThat(brandInSentToApprovalState().changeOwner(ownerCommand).getOwner()).isEqualTo(newOwner);
            assertThat(brandInApprovedState().changeOwner(ownerCommand).getOwner()).isEqualTo(newOwner);
            assertThat(brandInRejectedState().changeOwner(ownerCommand).getOwner()).isEqualTo(newOwner);
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
            var brand = BrandRoot.initializeForMerchant(createCommand);

            assertThat(brand.getUncommittedEvents()).isUnmodifiable();
        }

        @Test
        @DisplayName("Two different commands produce independent aggregates")
        void differentCommandsProduceIndependentAggregates() {
            var otherOwner = MerchantId.from(UUID.randomUUID());
            var otherCommand = BrandCreateCommand.builder()
                    .owner(otherOwner)
                    .name(new BrandName("Puma Brand1"))
                    .description(new BrandDescription("A well-known German sports brand"))
                    .path(new BrandPath("puma-brand"))
                    .build();

            var first = BrandRoot.initializeForMerchant(createCommand);
            var second = BrandRoot.initializeForMerchant(otherCommand);

            assertThat(first.getRootID().value()).isNotEqualTo(second.getRootID().value());
            assertThat(first.getOwner().value()).isNotEqualTo(second.getOwner().value());
            assertThat(first.getName()).isNotEqualTo(second.getName());
        }
    }
}