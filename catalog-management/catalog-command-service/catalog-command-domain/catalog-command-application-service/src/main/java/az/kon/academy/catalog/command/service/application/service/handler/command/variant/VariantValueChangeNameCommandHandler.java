package az.kon.academy.catalog.command.service.application.service.handler.command.variant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantValueCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantModerationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Variant.VARIANT_VALUE_CHANGE_NAME)
public class VariantValueChangeNameCommandHandler implements AbstractCommandHandler<VariantValueChangeNameCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final VariantModerationDomainService productVariantModerationDomainService;

    public VariantValueChangeNameCommandHandler(SeDomainContext domainContext,
                                                DomainEventPublisher domainEventPublisher,
                                                VariantModerationDomainService productVariantModerationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productVariantModerationDomainService = productVariantModerationDomainService;
    }

    @Override
    public Void handle(VariantValueChangeNameCommand command) {
        var aggregate = this.productVariantModerationDomainService.changeValueName(domainContext, command);
        var port = this.domainContext.getCommandPort(VariantValueCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
