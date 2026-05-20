package az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandCreateForMerchantCommand;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Brand.BRAND_MERCHANT_CREATE)
public class BrandCreateMerchantCommandHandler implements AbstractCommandHandler<BrandCreateForMerchantCommand, BrandCreateCommandResult> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandModificationDomainService brandModificationDomainService;

    public BrandCreateMerchantCommandHandler(SeDomainContext domainContext,
                                             DomainEventPublisher domainEventPublisher,
                                             BrandModificationDomainService brandModificationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandModificationDomainService = brandModificationDomainService;
    }

    @Override
    public BrandCreateCommandResult handle(BrandCreateForMerchantCommand command) {
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var brand = this.brandModificationDomainService.createForMerchant(domainContext, command);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return BrandCreateCommandResult.of(savedBrand);
    }
}
