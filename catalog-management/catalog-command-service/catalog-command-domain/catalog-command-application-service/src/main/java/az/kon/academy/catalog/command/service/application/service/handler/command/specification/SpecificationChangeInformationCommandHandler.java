package az.kon.academy.catalog.command.service.application.service.handler.command.specification;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.ProductSpecification.PRODUCT_SPECIFICATION_CHANGE_INFORMATION)
public class SpecificationChangeInformationCommandHandler implements AbstractCommandHandler<SpecificationChangeInformationCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductSpecificationDomainService productSpecificationDomainService;

    public SpecificationChangeInformationCommandHandler(SeDomainContext domainContext,
                                                        DomainEventPublisher domainEventPublisher,
                                                        ProductSpecificationDomainService productSpecificationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productSpecificationDomainService = productSpecificationDomainService;
    }

    @Override
    public Void handle(SpecificationChangeInformationCommand command) {
        var productSpecification = this.productSpecificationDomainService.changeInformation(domainContext, command);
        var productSpecificationPort = this.domainContext.getCommandPort(ProductSpecificationCommandPort.class);
        var savedProductSpecification = productSpecificationPort.save(productSpecification);
        this.domainEventPublisher.publish(productSpecification.getUncommittedEvents());
        return null;
    }
}
