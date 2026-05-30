package az.kon.academy.catalog.command.service.application.service.port.inbound.rest.brand;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonAddRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonChangeReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;

public interface BrandModerationInboundPort {

    BrandCreateCommandResult create(BrandCreateFromModerationRequest request);

    void approve(BrandApproveRequest request);

    void reject(BrandRejectRequest request);

    void changeOwner(BrandChangeOwnerRequest request);

    void changeImage(BrandChangeImageRequest request);

    void changeInformation(BrandChangeInformationRequest request);

    void changeToGlobal(BrandChangeToGlobalRequest request);

    void moveToInReview(BrandMoveToInReviewRequest request);

    void addRejectReason(BrandRejectionReasonAddRequest request);

    void removeRejectReason(BrandRejectionReasonDeleteRequest request);

    void changeRejectionReason(BrandRejectionReasonChangeReasonRequest request);
}
