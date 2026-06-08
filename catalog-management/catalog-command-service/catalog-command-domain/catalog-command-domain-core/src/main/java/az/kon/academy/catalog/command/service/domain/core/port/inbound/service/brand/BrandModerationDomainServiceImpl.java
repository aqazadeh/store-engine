package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.MerchantQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;
import java.util.Objects;

public final class BrandModerationDomainServiceImpl implements BrandModerationDomainService{

    @Override
    public BrandRoot create(final SeDomainContext context, final BrandCreateCommand command) {
        final var merchantQuery = context.getQueryPort(MerchantQueryOutboundPort.class);
        if(!Objects.isNull(command.getOwner())) {
            merchantQuery.checkMerchantIsActive(command.getOwner());
        }
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        brandQuery.checkExistsByName(command.getName());
        return BrandRoot.initializeForGlobal(command);
    }

    @Override
    public BrandRoot changeInformation(final SeDomainContext context, final BrandChangeInformationCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndIsGlobalTrue(command.getBrandId());
        return brand.changeInformation(command);
    }

    @Override
    public BrandRoot changeImage(final SeDomainContext context, final BrandChangeImageCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndIsGlobalTrue(command.getBrandId());
        return brand.changeImage(command);
    }

    @Override
    public BrandRoot changeOwner(final SeDomainContext context, final BrandChangeOwnerCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndIsGlobalTrue(command.getBrandId());
        return brand.changeOwner(command);
    }

    @Override
    public BrandRoot approve(final SeDomainContext context, final BrandApproveCommand command) {
        final var brandRejectionQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var existsReason = brandRejectionQuery.existsByBrandIdAndNotSolved(command.getBrandId());
        if(existsReason) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.HAS_UNSOLVED_REASON,
                    List.of(command.getBrandId().toString())
            );
        }

        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchById(command.getBrandId());
        return brand.approve();
    }

    @Override
    public BrandRoot reject(final SeDomainContext context, final BrandRejectCommand command) {
        final var brandRejectionQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var existsReason = brandRejectionQuery.existsByBrandIdAndNotSolved(command.getBrandId());
        if(!existsReason) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.AT_LEAST_ONE_REJECTION_REASON_REQUIRED,
                    List.of(command.getBrandId().toString())
            );
        }
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchById(command.getBrandId());
        return brand.reject();
    }

    @Override
    public BrandRoot changeToGlobal(final SeDomainContext context, final BrandChangeGlobalCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchById(command.getBrandId());
        return brand.changeGlobal(command);
    }

    @Override
    public BrandRoot moveToInReview(SeDomainContext context, BrandMoveToInReviewCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchById(command.getBrandId());
        return brand.moveToInReview();
    }
}
