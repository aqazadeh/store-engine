package az.kon.academy.catalog.command.service.application.service.handler.command.specification;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification.ProductSpecificationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.ProductSpecification.PRODUCT_SPECIFICATION_REMOVE_CATEGORY_ASSIGNMENT)
public class SpecificationRemoveCategoryAssignmentCommandHandler implements AbstractCommandHandler<SpecificationRemoveCategoryAssignmentCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductSpecificationDomainService productSpecificationDomainService;

    public SpecificationRemoveCategoryAssignmentCommandHandler(SeDomainContext domainContext,
                                                               DomainEventPublisher domainEventPublisher,
                                                               ProductSpecificationDomainService productSpecificationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productSpecificationDomainService = productSpecificationDomainService;
    }

    @Override
    public Void handle(SpecificationRemoveCategoryAssignmentCommand command) {
        var productSpecification = this.productSpecificationDomainService.removeCategoryAssignment(domainContext, command);
        var productSpecificationPort = this.domainContext.getCommandPort(ProductSpecificationCommandOutboundPort.class);
        var savedProductSpecification = productSpecificationPort.save(productSpecification);
        this.domainEventPublisher.publish(productSpecification.getUncommittedEvents());
        return null;
    }
}
