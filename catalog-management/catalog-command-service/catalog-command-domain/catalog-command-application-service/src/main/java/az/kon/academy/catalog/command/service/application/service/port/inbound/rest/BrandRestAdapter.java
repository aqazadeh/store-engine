package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.application.core.annotation.InputAdapter;
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
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.management.*;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandChangeImageCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandChangeInformationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandCreateMerchantCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandMoveToDraftCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant.BrandSentToApprovalCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateForGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandCreateForMerchantCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandMoveToDraftCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandSentToApprovalCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandPath;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.domain.core.security.SeSecurityContextHolder;

import java.util.Objects;

@InputAdapter
class BrandRestAdapter implements BrandRestPort {

    private final SeSecurityContextHolder securityContextHolder;
    private final BrandCreateGlobalCommandHandler brandCreateGlobalCommandHandler;
    private final BrandCreateMerchantCommandHandler brandCreateMerchantCommandHandler;
    private final BrandApproveCommandHandler brandApproveCommandHandler;
    private final BrandRejectCommandHandler brandRejectCommandHandler;
    private final BrandChangeOwnerCommandHandler brandChangeOwnerCommandHandler;
    private final BrandChangeImageCommandHandler brandChangeImageCommandHandler;
    private final BrandChangeInformationCommandHandler brandChangeInformationCommandHandler;
    private final BrandMoveToDraftCommandHandler brandMoveToDraftCommandHandler;
    private final BrandSentToApprovalCommandHandler brandSentToApprovalCommandHandler;

    public BrandRestAdapter(SeSecurityContextHolder securityContextHolder,
                            BrandCreateGlobalCommandHandler brandCreateGlobalCommandHandler,
                            BrandCreateMerchantCommandHandler brandCreateMerchantCommandHandler,
                            BrandApproveCommandHandler brandApproveCommandHandler,
                            BrandRejectCommandHandler brandRejectCommandHandler,
                            BrandChangeOwnerCommandHandler brandChangeOwnerCommandHandler,
                            BrandChangeImageCommandHandler brandChangeImageCommandHandler,
                            BrandChangeInformationCommandHandler brandChangeInformationCommandHandler,
                            BrandMoveToDraftCommandHandler brandMoveToDraftCommandHandler,
                            BrandSentToApprovalCommandHandler brandSentToApprovalCommandHandler) {
        this.securityContextHolder = securityContextHolder;
        this.brandCreateGlobalCommandHandler = brandCreateGlobalCommandHandler;
        this.brandCreateMerchantCommandHandler = brandCreateMerchantCommandHandler;
        this.brandApproveCommandHandler = brandApproveCommandHandler;
        this.brandRejectCommandHandler = brandRejectCommandHandler;
        this.brandChangeOwnerCommandHandler = brandChangeOwnerCommandHandler;
        this.brandChangeImageCommandHandler = brandChangeImageCommandHandler;
        this.brandChangeInformationCommandHandler = brandChangeInformationCommandHandler;
        this.brandMoveToDraftCommandHandler = brandMoveToDraftCommandHandler;
        this.brandSentToApprovalCommandHandler = brandSentToApprovalCommandHandler;
    }

    @Override
    public BrandCreateCommandResult createGlobal(BrandCreateGlobalRequest request) {
        var command = BrandCreateForGlobalCommand.builder()
                .owner(Objects.isNull(request.getOwner()) ? null : MerchantId.from(request.getOwner()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();
        return this.brandCreateGlobalCommandHandler.handle(command);
    }

    @Override
    public BrandCreateCommandResult createMerchant(BrandCreateForMerchantRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandCreateForMerchantCommand.builder()
                .owner(MerchantId.from(currentUser))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();
        return this.brandCreateMerchantCommandHandler.handle(command);
    }

    @Override
    public void approve(BrandApproveRequest request) {
        var command = BrandApproveCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandApproveCommandHandler.handle(command);
    }

    @Override
    public void reject(BrandRejectRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId(); // Fixme  bug check role to
        var command = BrandCreateRejectionReasonCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .reason(request.getReason())
                .moderatedBy(ModeratorId.from(currentUser))
                .build();
        this.brandRejectCommandHandler.handle(command);
    }

    @Override
    public void changeOwner(BrandChangeOwnerRequest request) {
        var command = BrandChangeOwnerCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .owner(MerchantId.from(request.getOwner()))
                .build();
        this.brandChangeOwnerCommandHandler.handle(command);
    }

    @Override
    public void changeImage(BrandChangeImageRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandChangeImageCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .image(request.getImage())
                .build();
        this.brandChangeImageCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(BrandChangeInformationRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandChangeInformationCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .build();
        this.brandChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void moveToDraft(BrandMoveToDraftRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandMoveToDraftCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandMoveToDraftCommandHandler.handle(command);
    }

    @Override
    public void sentToApproval(BrandSentToApprovalRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = BrandSentToApprovalCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandSentToApprovalCommandHandler.handle(command);
    }
}
