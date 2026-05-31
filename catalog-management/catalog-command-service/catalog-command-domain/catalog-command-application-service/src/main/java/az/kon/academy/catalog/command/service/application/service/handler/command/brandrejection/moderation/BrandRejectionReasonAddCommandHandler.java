package az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Brand.BRAND_MODERATION_ADD_REJECTION_REASON;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR;

@CommandHandler(roles = ROLE_DOMAIN_MODERATOR, permissions = BRAND_MODERATION_ADD_REJECTION_REASON)
public class BrandRejectionReasonAddCommandHandler implements AbstractCommandHandler<BrandRejectionReasonAddCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandRejectionModerationDomainService brandRejectionModerationDomainService;

    public BrandRejectionReasonAddCommandHandler(SeDomainContext domainContext,
                                                 DomainEventPublisher domainEventPublisher,
                                                 BrandRejectionModerationDomainService brandRejectionModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandRejectionModerationDomainService = brandRejectionModerationDomainService;
    }

    @Override
    public Void handle(BrandRejectionReasonAddCommand command) {
        var brandRejectionReasonCommandPort = this.domainContext.getCommandPort(BrandRejectionReasonCommandOutboundPort.class);
        var brandRejectionReason = this.brandRejectionModerationDomainService.add(domainContext, command);
        var savedBrandRejectionReason = brandRejectionReasonCommandPort.save(brandRejectionReason);
        this.domainEventPublisher.publish(brandRejectionReason.getUncommittedEvents());
        return null;
    }
}
