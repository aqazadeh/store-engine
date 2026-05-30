package az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.moderation;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Brand.BRAND_MODERATION_CHANGE_REJECTION_REASON)
public class BrandRejectionReasonChangeReasonCommandHandler implements AbstractCommandHandler<BrandRejectionReasonChangeReasonCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandRejectionModerationDomainService brandRejectionModerationDomainService;

    public BrandRejectionReasonChangeReasonCommandHandler(SeDomainContext domainContext,
                                                          DomainEventPublisher domainEventPublisher,
                                                          BrandRejectionModerationDomainService brandRejectionModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandRejectionModerationDomainService = brandRejectionModerationDomainService;
    }

    @Override
    public Void handle(BrandRejectionReasonChangeReasonCommand command) {
        var brandRejectionReasonCommandPort = this.domainContext.getCommandPort(BrandRejectionReasonCommandOutboundPort.class);
        var brandRejectionReason = this.brandRejectionModerationDomainService.changeReason(domainContext, command);
        var savedBrandRejectionReason = brandRejectionReasonCommandPort.save(brandRejectionReason);
        this.domainEventPublisher.publish(brandRejectionReason.getUncommittedEvents());
        return null;
    }
}
