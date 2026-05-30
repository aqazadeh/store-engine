package az.kon.academy.catalog.command.service.application.service.handler.command.brand.moderation;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Brand.BRAND_MODERATION_CHANGE_OWNER)
public class BrandChangeOwnerCommandHandler implements AbstractCommandHandler<BrandChangeOwnerCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandModerationDomainService brandModerationDomainService;

    public BrandChangeOwnerCommandHandler(SeDomainContext domainContext,
                                          DomainEventPublisher domainEventPublisher,
                                          BrandModerationDomainService brandModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandModerationDomainService = brandModerationDomainService;
    }

    @Override
    public Void handle(BrandChangeOwnerCommand command) {
        var brand = this.brandModerationDomainService.changeOwner(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandOutboundPort.class);
        var savedBrand = brandCommandPort.save(brand);

        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}
