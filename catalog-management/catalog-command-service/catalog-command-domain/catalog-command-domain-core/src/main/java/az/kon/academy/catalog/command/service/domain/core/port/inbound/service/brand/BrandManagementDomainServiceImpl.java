package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.rules.BrandDomainRules;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class BrandManagementDomainServiceImpl implements BrandManagementDomainService {

    @Override
    public BrandRoot create(final SeDomainContext context, final BrandCreateCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var merchantBrandCount = brandQuery.fetchCountByMerchantId(command.getOwner());
        if (merchantBrandCount >= BrandDomainRules.MAX_BRANDS_PER_MERCHANT) {
            throw new BrandDomainException(BrandDomainErrorCodes.TOO_MANY_BRANDS_FOR_MERCHANT, List.of(command.getOwner().toString()));
        }
        brandQuery.checkExistsByName(command.getName());
        return BrandRoot.initializeForMerchant(command);
    }

    @Override
    public BrandRoot changeInformation(final SeDomainContext context, final BrandChangeInformationCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndMerchantId(command.getBrandId(), command.getOwner());
        return brand.changeInformation(command);
    }

    @Override
    public BrandRoot changeImage(final SeDomainContext context, final BrandChangeImageCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndMerchantId(command.getBrandId(), command.getOwner());
        return brand.changeImage(command);
    }

    @Override
    public BrandRoot sentToApproval(final SeDomainContext context, final BrandSentToApprovalCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndMerchantId(command.getBrandId(), command.getOwner());
        final var brandRejectionReasonQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        if(brandRejectionReasonQuery.existsByBrandIdAndNotSolved(command.getBrandId())) {
            throw new BrandDomainException(BrandDomainErrorCodes.HAS_UNSOLVED_REASON);
        }
        return brand.sentToApproval();
    }

    @Override
    public BrandRoot moveToDraft(final SeDomainContext context, final BrandMoveToDraftCommand command) {
        final var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        final var brand = brandQuery.fetchByIdAndMerchantId(command.getBrandId(), command.getOwner());
        return brand.moveToDraft();
    }
}
