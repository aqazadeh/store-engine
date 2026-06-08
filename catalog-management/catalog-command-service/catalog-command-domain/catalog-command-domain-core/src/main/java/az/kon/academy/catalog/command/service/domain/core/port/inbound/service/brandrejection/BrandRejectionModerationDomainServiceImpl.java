package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class BrandRejectionModerationDomainServiceImpl implements BrandRejectionModerationDomainService{

    @Override
    public BrandRejectionReasonRoot add(SeDomainContext context, BrandRejectionReasonAddCommand command) {
        checkBrandStatusForRejection(context, command.getBrandId());
        return BrandRejectionReasonRoot.initialize(command);
    }

    @Override
    public BrandRejectionReasonRoot remove(SeDomainContext context, BrandRejectionReasonRemoveCommand command) {
        final var brandRejectionReasonQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var brandRejectionReason = brandRejectionReasonQuery.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        checkBrandStatusForRejection(context, brandRejectionReason.getBrandId());
        return brandRejectionReason.remove(command);
    }

    @Override
    public BrandRejectionReasonRoot changeReason(SeDomainContext context, BrandRejectionReasonChangeReasonCommand command) {
        final var brandRejectionReasonQueryPort = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var brandRejectionReason = brandRejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        checkBrandStatusForRejection(context, brandRejectionReason.getBrandId());
        return brandRejectionReason.changeReason(command);
    }

    private void checkBrandStatusForRejection(SeDomainContext context, BrandId brandId) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchById(brandId);
        if (!brand.getStatus().isInReview() && !brand.getStatus().isSentToApproval()) {
            throw new BrandDomainException(
                    BrandDomainErrorCodes.STATUS_INVALID_FOR_REJECTION_OPERATION,
                    List.of(brandId.toString())
            );
        }
    }
}
