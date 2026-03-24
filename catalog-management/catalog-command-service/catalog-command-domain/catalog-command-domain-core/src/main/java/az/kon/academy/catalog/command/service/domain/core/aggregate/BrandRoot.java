package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder(toBuilder = true)
public class BrandRoot extends AggregateRoot<BrandRoot, BrandId> {
    @Getter
    private final MerchantId merchantId;
    @Getter
    private BrandName name;
    @Getter
    private BrandDescription description;
    @Getter
    private BrandPath path;
    @Getter
    private String image; //Fixme when file storage is implemented, change it to Image value object
    @Getter
    private BrandStatus status;

    public static BrandRoot initialize(BrandCreateCommand command) {
        return BrandRoot.builder()
                .id(BrandId.random())
                .merchantId(command.getMerchantId())
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .status(BrandStatus.DRAFT)
                .build();
    }

    public BrandRoot approve() {
        return this.toBuilder()
                .status(BrandStatus.APPROVED)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for brand approval
    }

    public BrandRoot reject() {
        return this.toBuilder()
                .status(BrandStatus.REJECTED)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for brand rejection
    }

    public BrandRoot sentToApproval() {

        if (!this.status.isDraft() || !this.status.isRejected()) {
            throw new BrandDomainException(BrandDomainErrorCodes.STATUS_INVALID_FOR_APPROVAL, List.of(this.getRootID().toString()));
        }

        return this.toBuilder()
                .status(BrandStatus.SENT_TO_APPROVAL)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for brand sent to approval
    }

    public BrandRoot moveToDraft() {

        if (!this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_MOVE_TO_DRAFT,
                    List.of(this.getRootID().toString())
            );
        }

        return this.toBuilder()
                .status(BrandStatus.DRAFT)
                .modificationTs(SeDateTime.now())
                .build();
        //Fixme when implement event system, add event for brand move to draft
    }

    public BrandRoot changeInformation(BrandChangeInformationCommand command) {

        if (this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }

        return this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .path(command.getPath())
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for brand information change
    }

    public BrandRoot changeImage(BrandChangeImageCommand command) {

        if (this.status.isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }

        return this.toBuilder()
                .image(command.getImage())
                .modificationTs(SeDateTime.now())
                .build();

        //Fixme when implement event system, add event for brand image change
    }

}
