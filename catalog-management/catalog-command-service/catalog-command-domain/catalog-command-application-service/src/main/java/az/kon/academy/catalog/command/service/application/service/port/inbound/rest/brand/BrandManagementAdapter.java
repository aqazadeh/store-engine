package az.kon.academy.catalog.command.service.application.service.port.inbound.rest.brand;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonSolveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.handler.command.brand.management.*;
import az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.management.BrandRejectionReasonSolveCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.security.SeSecurityContextHolder;

@InputAdapter
class BrandManagementAdapter implements BrandManagementPort {

    private final SeSecurityContextHolder securityContextHolder;
    private final BrandCreateCommandHandler brandCreateCommandHandler;
    private final BrandChangeImageCommandHandler brandChangeImageCommandHandler;
    private final BrandChangeInformationCommandHandler brandChangeInformationCommandHandler;
    private final BrandMoveToDraftCommandHandler brandMoveToDraftCommandHandler;
    private final BrandSentToApprovalCommandHandler brandSentToApprovalCommandHandler;
    private final BrandRejectionReasonSolveCommandHandler brandRejectionReasonSolveCommandHandler;

    public BrandManagementAdapter(SeSecurityContextHolder securityContextHolder,
                                  BrandCreateCommandHandler brandCreateCommandHandler,
                                  BrandChangeImageCommandHandler brandChangeImageCommandHandler,
                                  BrandChangeInformationCommandHandler brandChangeInformationCommandHandler,
                                  BrandMoveToDraftCommandHandler brandMoveToDraftCommandHandler,
                                  BrandSentToApprovalCommandHandler brandSentToApprovalCommandHandler,
                                  BrandRejectionReasonSolveCommandHandler brandRejectionReasonSolveCommandHandler) {
        this.securityContextHolder = securityContextHolder;
        this.brandCreateCommandHandler = brandCreateCommandHandler;
        this.brandChangeImageCommandHandler = brandChangeImageCommandHandler;
        this.brandChangeInformationCommandHandler = brandChangeInformationCommandHandler;
        this.brandMoveToDraftCommandHandler = brandMoveToDraftCommandHandler;
        this.brandSentToApprovalCommandHandler = brandSentToApprovalCommandHandler;
        this.brandRejectionReasonSolveCommandHandler = brandRejectionReasonSolveCommandHandler;
    }

    @Override
    public BrandCreateCommandResult create(final BrandCreateRequest request) {
        final var currentUser = this.securityContextHolder.getUser().getUserId();
        final var command = BrandCreateCommand.builder()
                .owner(MerchantId.from(currentUser))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .path(BrandPath.create(request.getName()))
                .build();
        return this.brandCreateCommandHandler.handle(command);
    }

    @Override
    public void changeImage(BrandChangeImageRequest request) {
        final var currentUser = this.securityContextHolder.getUser().getUserId();
        final var command = BrandChangeImageCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .image(request.getImage())
                .build();
        this.brandChangeImageCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(BrandChangeInformationRequest request) {
        final var currentUser = this.securityContextHolder.getUser().getUserId();
        final var command = BrandChangeInformationCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .name(BrandName.of(request.getName()))
                .description(BrandDescription.of(request.getDescription()))
                .build();
        this.brandChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void moveToDraft(BrandMoveToDraftRequest request) {
        final var currentUser = this.securityContextHolder.getUser().getUserId();
        final var command = BrandMoveToDraftCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandMoveToDraftCommandHandler.handle(command);
    }

    @Override
    public void sentToApproval(BrandSentToApprovalRequest request) {
        final var currentUser = this.securityContextHolder.getUser().getUserId();
        final var command = BrandSentToApprovalCommand.builder()
                .owner(MerchantId.from(currentUser))
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.brandSentToApprovalCommandHandler.handle(command);
    }

    @Override
    public void solveRejectionReason(BrandRejectionReasonSolveRequest request) {

        final var userId = this.securityContextHolder.getUser().getUserId();
        final var merchantId = MerchantId.from(userId);

        final var command = BrandRejectionReasonSolveCommand
                .builder()
                .brandRejectionReasonId(BrandRejectionReasonId.from(request.getBrandRejectionReasonId()))
                .merchantId(merchantId)
                .build();
        this.brandRejectionReasonSolveCommandHandler.handle(command);
    }
}
