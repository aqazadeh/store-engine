package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandMoveToDraftCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Brand.BRAND_MANAGEMENT_MOVE_DRAFT)
public class BrandMoveToDraftCommandHandler implements AbstractCommandHandler<BrandMoveToDraftCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandMoveToDraftCommandHandler(SeDomainContext domainContext,
                                          DomainEventPublisher domainEventPublisher,
                                          BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public Void handle(BrandMoveToDraftCommand command) {
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandOutboundPort.class);
        var brand = this.brandManagementDomainService.moveToDraft(domainContext, command);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}
