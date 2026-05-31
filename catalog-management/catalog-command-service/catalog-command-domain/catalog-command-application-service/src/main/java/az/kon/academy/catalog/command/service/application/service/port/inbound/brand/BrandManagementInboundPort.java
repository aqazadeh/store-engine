package az.kon.academy.catalog.command.service.application.service.port.inbound.brand;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonSolveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;

public interface BrandManagementInboundPort {

    BrandCreateCommandResult create(BrandCreateRequest request);

    void changeImage(BrandChangeImageRequest request);

    void changeInformation(BrandChangeInformationRequest request);

    void moveToDraft(BrandMoveToDraftRequest request);

    void sentToApproval(BrandSentToApprovalRequest request);

    void solveRejectionReason(BrandRejectionReasonSolveRequest request);
}
