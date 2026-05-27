package az.kon.academy.catalog.command.service.application.service.handler.command.specification;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.ProductSpecification.PRODUCT_SPECIFICATION_CREATE)
public class SpecificationCreateCommandHandler implements AbstractCommandHandler<SpecificationCreateCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductSpecificationDomainService productSpecificationDomainService;

    public SpecificationCreateCommandHandler(SeDomainContext domainContext,
                                                        DomainEventPublisher domainEventPublisher,
                                                        ProductSpecificationDomainService productSpecificationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productSpecificationDomainService = productSpecificationDomainService;
    }

    @Override
    public Void handle(SpecificationCreateCommand command) {
        var productSpecification = this.productSpecificationDomainService.create(domainContext, command);
        var productSpecificationPort = this.domainContext.getCommandPort(ProductSpecificationCommandPort.class);
        var savedProductSpecification = productSpecificationPort.save(productSpecification);
        this.domainEventPublisher.publish(productSpecification.getUncommittedEvents());
        return null;
    }
}
