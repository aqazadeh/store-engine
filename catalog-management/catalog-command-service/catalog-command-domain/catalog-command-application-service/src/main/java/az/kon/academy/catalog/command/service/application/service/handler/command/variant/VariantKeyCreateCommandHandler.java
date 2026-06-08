package az.kon.academy.catalog.command.service.application.service.handler.command.variant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantKeyCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Variant.VARIANT_KEY_CREATE)
public class VariantKeyCreateCommandHandler implements AbstractCommandHandler<VariantKeyCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final VariantModerationDomainService productVariantModerationDomainService;

    public VariantKeyCreateCommandHandler(SeDomainContext domainContext,
                                          DomainEventPublisher domainEventPublisher,
                                          VariantModerationDomainService productVariantModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productVariantModerationDomainService = productVariantModerationDomainService;
    }

    @Override
    public Void handle(VariantKeyCreateCommand command) {
        var aggregate = this.productVariantModerationDomainService.createKey(domainContext, command);
        var port = this.domainContext.getCommandPort(VariantKeyCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
