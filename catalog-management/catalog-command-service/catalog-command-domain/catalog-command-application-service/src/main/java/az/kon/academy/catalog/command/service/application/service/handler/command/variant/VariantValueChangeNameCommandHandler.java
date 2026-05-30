package az.kon.academy.catalog.command.service.application.service.handler.command.variant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantValueCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantValueChangeNameCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant.VariantDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Variant.VARIANT_VALUE_CHANGE_NAME)
public class VariantValueChangeNameCommandHandler implements AbstractCommandHandler<VariantValueChangeNameCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final VariantDomainService variantDomainService;

    public VariantValueChangeNameCommandHandler(SeDomainContext domainContext,
                                                DomainEventPublisher domainEventPublisher,
                                                VariantDomainService variantDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.variantDomainService = variantDomainService;
    }

    @Override
    public Void handle(VariantValueChangeNameCommand command) {
        var aggregate = this.variantDomainService.changeValueName(domainContext, command);
        var port = this.domainContext.getCommandPort(VariantValueCommandPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
