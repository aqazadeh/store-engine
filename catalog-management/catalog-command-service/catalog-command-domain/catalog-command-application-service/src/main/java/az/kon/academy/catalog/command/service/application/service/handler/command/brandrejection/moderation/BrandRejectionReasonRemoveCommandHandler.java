package az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Brand.BRAND_MODERATION_REMOVE_REJECTION_REASON)
public class BrandRejectionReasonRemoveCommandHandler implements AbstractCommandHandler<BrandRejectionReasonRemoveCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandRejectionModerationDomainService brandRejectionModerationDomainService;

    public BrandRejectionReasonRemoveCommandHandler(SeDomainContext domainContext,
                                                    DomainEventPublisher domainEventPublisher,
                                                    BrandRejectionModerationDomainService brandRejectionModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandRejectionModerationDomainService = brandRejectionModerationDomainService;
    }

    @Override
    public Void handle(BrandRejectionReasonRemoveCommand command) {
        var brandRejectionReasonCommandPort = this.domainContext.getCommandPort(BrandRejectionReasonCommandOutboundPort.class);
        var brandRejectionReason = this.brandRejectionModerationDomainService.remove(domainContext, command);
        var savedBrandRejectionReason = brandRejectionReasonCommandPort.save(brandRejectionReason);
        this.domainEventPublisher.publish(brandRejectionReason.getUncommittedEvents());
        return null;
    }
}
