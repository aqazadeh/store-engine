package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Brand.BRAND_MANAGEMENT_CHANGE_IMAGE;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role.ROLE_MERCHANT;

@CommandHandler(roles = ROLE_MERCHANT, permissions = BRAND_MANAGEMENT_CHANGE_IMAGE)
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
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandOutboundPort.class);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}