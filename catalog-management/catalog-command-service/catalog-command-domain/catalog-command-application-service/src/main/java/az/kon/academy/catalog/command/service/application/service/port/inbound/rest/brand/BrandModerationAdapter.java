package az.kon.academy.catalog.command.service.application.service.port.inbound.rest.brand;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonAddRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonChangeReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.moderation.*;
import az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation.BrandRejectionReasonAddCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation.BrandRejectionReasonChangeReasonCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation.BrandRejectionReasonRemoveCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.domain.core.security.SeSecurityContextHolder;

import java.util.Objects;

@InputAdapter
public class BrandModerationAdapter implements BrandModerationPort {

    private final SeSecurityContextHolder securityContextHolder;

    private final BrandCreateFromModerationCommandHandler brandCreateFromModerationCommandHandler;
    private final BrandApproveCommandHandler brandApproveCommandHandler;
    private final BrandRejectCommandHandler brandRejectCommandHandler;
    private final BrandChangeOwnerCommandHandler brandChangeOwnerCommandHandler;
    private final BrandChangeImageFromModerationCommandHandler brandChangeImageFromModerationCommandHandler;
    private final BrandChangeInformationFromModerationCommandHandler brandChangeInformationFromModerationCommandHandler;
    private final BrandChangeGlobalCommandHandler brandChangeGlobalCommandHandler;
    private final BrandMoveToInReviewCommandHandler brandMoveToInReviewCommandHandler;

    private final BrandRejectionReasonAddCommandHandler brandRejectionReasonAddCommandHandler;
    private final BrandRejectionReasonChangeReasonCommandHandler brandRejectionReasonChangeReasonCommandHandler;
    private final BrandRejectionReasonRemoveCommandHandler brandRejectionReasonRemoveCommandHandler;

    public BrandModerationAdapter(SeSecurityContextHolder securityContextHolder,
                                  BrandCreateFromModerationCommandHandler brandCreateFromModerationCommandHandler,
                                  BrandApproveCommandHandler brandApproveCommandHandler,
                                  BrandRejectCommandHandler brandRejectCommandHandler,
                                  BrandChangeOwnerCommandHandler brandChangeOwnerCommandHandler,
                                  BrandChangeImageFromModerationCommandHandler brandChangeImageFromModerationCommandHandler,
                                  BrandChangeInformationFromModerationCommandHandler brandChangeInformationFromModerationCommandHandler,
                                  BrandChangeGlobalCommandHandler brandChangeGlobalCommandHandler,
                                  BrandMoveToInReviewCommandHandler brandMoveToInReviewCommandHandler,
                                  BrandRejectionReasonAddCommandHandler brandRejectionReasonAddCommandHandler,
                                  BrandRejectionReasonChangeReasonCommandHandler brandRejectionReasonChangeReasonCommandHandler,
                                  BrandRejectionReasonRemoveCommandHandler brandRejectionReasonRemoveCommandHandler) {
        this.securityContextHolder = securityContextHolder;
        this.brandCreateFromModerationCommandHandler = brandCreateFromModerationCommandHandler;
        this.brandApproveCommandHandler = brandApproveCommandHandler;
        this.brandRejectCommandHandler = brandRejectCommandHandler;
        this.brandChangeOwnerCommandHandler = brandChangeOwnerCommandHandler;
        this.brandChangeImageFromModerationCommandHandler = brandChangeImageFromModerationCommandHandler;
        this.brandChangeInformationFromModerationCommandHandler = brandChangeInformationFromModerationCommandHandler;
        this.brandChangeGlobalCommandHandler = brandChangeGlobalCommandHandler;
        this.brandMoveToInReviewCommandHandler = brandMoveToInReviewCommandHandler;
        this.brandRejectionReasonAddCommandHandler = brandRejectionReasonAddCommandHandler;
        this.brandRejectionReasonChangeReasonCommandHandler = brandRejectionReasonChangeReasonCommandHandler;
        this.brandRejectionReasonRemoveCommandHandler = brandRejectionReasonRemoveCommandHandler;
    }

    @Override
    public BrandCreateCommandResult create(BrandCreateFromModerationRequest request) {
        final var command = BrandCreateCommand.builder()
                .owner(Objects.isNull(request.getOwner()) ? null : MerchantId.from(request.getOwner()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();
        return this.brandCreateFromModerationCommandHandler.handle(command);
    }

    @Override
    public void approve(BrandApproveRequest request) {
        final var command = BrandApproveCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandApproveCommandHandler.handle(command);
    }

    @Override
    public void reject(BrandRejectRequest request) {
        final var command = BrandRejectCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandRejectCommandHandler.handle(command);
    }

    @Override
    public void changeOwner(BrandChangeOwnerRequest request) {
        final var command = BrandChangeOwnerCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .owner(MerchantId.from(request.getOwner()))
                .build();
        this.brandChangeOwnerCommandHandler.handle(command);
    }

    @Override
    public void changeImage(BrandChangeImageRequest request) {
        final var command = BrandChangeImageCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .image(request.getImage())
                .build();
        this.brandChangeImageFromModerationCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(BrandChangeInformationRequest request) {
        final var command = BrandChangeInformationCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .build();
        this.brandChangeInformationFromModerationCommandHandler.handle(command);
    }

    @Override
    public void changeToGlobal(BrandChangeToGlobalRequest request) {
        final var command = BrandChangeGlobalCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandChangeGlobalCommandHandler.handle(command);
    }

    @Override
    public void moveToInReview(BrandMoveToInReviewRequest request) {
        final var command = BrandMoveToInReviewCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandMoveToInReviewCommandHandler.handle(command);
    }

    @Override
    public void addRejectReason(BrandRejectionReasonAddRequest request) {
        var userId = this.securityContextHolder.getUser().getUserId();
        var moderatorId = ModeratorId.from(userId);
        final var command = BrandRejectionReasonAddCommand.builder()
                .brandId(BrandId.from(request.getBrandId()))
                .moderatorId(moderatorId)
                .reason(request.getReason())
                .build();
        this.brandRejectionReasonAddCommandHandler.handle(command);
    }

    @Override
    public void removeRejectReason(BrandRejectionReasonDeleteRequest request) {
        var userId = this.securityContextHolder.getUser().getUserId();
        var moderatorId = ModeratorId.from(userId);
        final var command = BrandRejectionReasonRemoveCommand.builder()
                .brandRejectionReasonId(BrandRejectionReasonId.from(request.getBrandRejectionReasonId()))
                .moderatorId(moderatorId)
                .build();
        this.brandRejectionReasonRemoveCommandHandler.handle(command);
    }

    @Override
    public void changeRejectionReason(BrandRejectionReasonChangeReasonRequest request) {
        var userId = this.securityContextHolder.getUser().getUserId();
        var moderatorId = ModeratorId.from(userId);
        final var command = BrandRejectionReasonChangeReasonCommand.builder()
                .brandRejectionReasonId(BrandRejectionReasonId.from(request.getBrandRejectionReasonId()))
                .moderatorId(moderatorId)
                .reason(request.getReason())
                .build();
        this.brandRejectionReasonChangeReasonCommandHandler.handle(command);
    }
}
