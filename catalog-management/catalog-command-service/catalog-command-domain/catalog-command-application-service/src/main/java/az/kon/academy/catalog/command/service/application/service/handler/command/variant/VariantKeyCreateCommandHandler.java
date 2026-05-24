package az.kon.academy.catalog.command.service.application.service.handler.command.variant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantKeyCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.variant.VariantKeyCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.service.variant.VariantDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Variant.VARIANT_KEY_CREATE)
public class VariantKeyCreateCommandHandler implements AbstractCommandHandler<VariantKeyCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final VariantDomainService variantDomainService;

    public VariantKeyCreateCommandHandler(SeDomainContext domainContext,
                                          DomainEventPublisher domainEventPublisher,
                                          VariantDomainService variantDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.variantDomainService = variantDomainService;
    }

    @Override
    public Void handle(VariantKeyCreateCommand command) {
        var aggregate = this.variantDomainService.createKey(domainContext, command);
        var port = this.domainContext.getCommandPort(VariantKeyCommandPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
