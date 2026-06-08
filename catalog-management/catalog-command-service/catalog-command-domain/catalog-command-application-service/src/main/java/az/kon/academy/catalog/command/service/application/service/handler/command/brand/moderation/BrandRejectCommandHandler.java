package az.kon.academy.catalog.command.service.application.service.handler.command.brand.moderation;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandRejectCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Brand.BRAND_MODERATION_REJECT;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role;

@CommandHandler(roles = Role.ROLE_DOMAIN_MODERATOR, permissions = BRAND_MODERATION_REJECT)
public class BrandRejectCommandHandler implements AbstractCommandHandler<BrandRejectCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandModerationDomainService brandModerationDomainService;

    public BrandRejectCommandHandler(SeDomainContext domainContext,
                                     DomainEventPublisher domainEventPublisher,
                                     BrandModerationDomainService brandModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandModerationDomainService = brandModerationDomainService;
    }

    @Override
    public Void handle(BrandRejectCommand command) {
        var brand = this.brandModerationDomainService.reject(domainContext, command);
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandOutboundPort.class);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }

}
