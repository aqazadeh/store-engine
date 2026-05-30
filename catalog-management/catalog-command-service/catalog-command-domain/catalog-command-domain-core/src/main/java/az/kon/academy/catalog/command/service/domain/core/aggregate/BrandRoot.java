package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.event.brand.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class BrandRoot extends AggregateRoot<BrandRoot, BrandId> {

    @Getter private final MerchantId owner;
    @Getter private BrandName name;
    @Getter private BrandDescription description;
    @Getter private BrandPath path;
    @Getter private String image; //Fixme when file storage is implemented, change it to Image value object
    @Getter private Boolean isGlobal;
    @Getter private BrandStatus status;

    public static BrandRoot initializeForMerchant(BrandCreateCommand command) {
        var brand = BrandRoot.builder()
                .id(BrandId.random())
                .owner(command.getOwner())
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .isGlobal(Boolean.FALSE)
                .status(BrandStatus.DRAFT)
                .build();
        var event = BrandCreatedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getOwner().value(),
                brand.getName().value(),
                brand.getDescription().value(),
                brand.getPath().value(),
                brand.getIsGlobal(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public static BrandRoot initializeForGlobal(BrandCreateCommand command) {
        var brand = BrandRoot.builder()
                .id(BrandId.random())
                .owner(command.getOwner())
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .isGlobal(Boolean.TRUE)
                .status(BrandStatus.APPROVED)
                .build();
        var event = BrandCreatedGlobalEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getOwner().value(),
                brand.getName().value(),
                brand.getDescription().value(),
                brand.getPath().value(),
                brand.getIsGlobal(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot approve() {

        if(!this.status.isInReview()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_APPROVE,
                    List.of(this.getRootID().toString())
            );
        }

        var brand = this.toBuilder()
                .status(BrandStatus.APPROVED)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandApprovedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot reject() {

        if(!this.status.isInReview()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_REJECT,
                    List.of(this.getRootID().toString())
            );
        }

        var brand = this.toBuilder()
                .status(BrandStatus.REJECTED)
                .modificationTs(SeDateTime.now())
                .build();
        var event = BrandRejectedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot sentToApproval() {

        if (!this.status.isDraft() && !this.status.isRejected()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }

        var brand = this.toBuilder()
                .status(BrandStatus.SENT_TO_APPROVAL)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandSentToApprovalEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot moveToDraft() {

        if (!this.status.isSentToApproval() || !this.status.isRejected()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_MOVE_TO_DRAFT,
                    List.of(this.getRootID().toString())
            );
        }

        var brand = this.toBuilder()
                .status(BrandStatus.DRAFT)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandMovedToDraftEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot moveToInReview() {
        if(!this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_MOVE_TO_IN_REVIEW,
                    List.of(this.getRootID().toString())
            );
        }
        var brand = this.toBuilder()
                .status(BrandStatus.IN_REVIEW)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandMovedToInReviewEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getStatus().name()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot changeInformation(BrandChangeInformationCommand command) {

        if (this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_SENT_TO_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }
        var brand = this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandInformationChangedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getName().value(),
                brand.getDescription().value()
        );
        brand.addEvent(event);

        return brand;
    }

    public BrandRoot changeImage(BrandChangeImageCommand command) {

        if (this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_SENT_TO_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }
        var brand = this.toBuilder()
                .image(command.getImage())
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandImageChangedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getImage()
        );

        brand.addEvent(event);
        return brand;
    }

    public BrandRoot changeOwner(BrandChangeOwnerCommand command) {
        var brand = this.toBuilder()
                .owner(command.getOwner())
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandOwnerChangedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getOwner().value()
        );
        brand.addEvent(event);
        return brand;
    }

    public BrandRoot changeGlobal(BrandChangeGlobalCommand command) {
        var brand = this.toBuilder()
                .isGlobal(Boolean.TRUE)
                .modificationTs(SeDateTime.now())
                .build();

        var event = BrandToGlobalChangedEvent.of(
                brand.getRootID().value().toString(),
                brand.getModificationTs().toOffsetDateTime(),
                brand.getIsGlobal()
        );
        brand.addEvent(event);
        return brand;
    }

}
