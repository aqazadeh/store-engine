package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandApproveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandChangeOwnerRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandCreateGlobalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandRejectRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandCreateForMerchantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandMoveToDraftRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.BrandSentToApprovalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;

public interface BrandRestPort {

    BrandCreateCommandResult createGlobalBrand(BrandCreateGlobalRequest request);

    BrandCreateCommandResult createMerchantBrand(BrandCreateForMerchantRequest request);

    void approveBrand(BrandApproveRequest request);

    void rejectBrand(BrandRejectRequest request);

    void changeOwner(BrandChangeOwnerRequest request);

    void changeImage(BrandChangeImageRequest request);

    void changeInformation(BrandChangeInformationRequest request);

    void moveToDraft(BrandMoveToDraftRequest request);

    void sentToApproval(BrandSentToApprovalRequest request);
}
