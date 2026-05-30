package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Brand.BRAND_MANAGEMENT_CHANGE_IMAGE)
public class BrandChangeImageCommandHandler implements AbstractCommandHandler<BrandChangeImageCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandChangeImageCommandHandler(SeDomainContext domainContext,
                                          DomainEventPublisher domainEventPublisher,
                                          BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public Void handle(BrandChangeImageCommand command) {
        var brand = this.brandManagementDomainService.changeImage(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}