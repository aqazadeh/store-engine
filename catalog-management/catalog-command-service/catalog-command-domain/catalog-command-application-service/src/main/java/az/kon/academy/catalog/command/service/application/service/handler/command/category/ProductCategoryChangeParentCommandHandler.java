package az.kon.academy.catalog.command.service.application.service.handler.command.category;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.category.ProductCategoryChangeParentCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category.ProductCategoryDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.ProductCategory.PRODUCT_CATEGORY_CHANGE_PARENT)
public class ProductCategoryChangeParentCommandHandler implements AbstractCommandHandler<ProductCategoryChangeParentCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductCategoryDomainService productCategoryDomainService;

    public ProductCategoryChangeParentCommandHandler(SeDomainContext domainContext,
                                                     DomainEventPublisher domainEventPublisher,
                                                     ProductCategoryDomainService productCategoryDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productCategoryDomainService = productCategoryDomainService;
    }

    @Override
    public Void handle(ProductCategoryChangeParentCommand command) {
        var productCategory = this.productCategoryDomainService.changeParent(domainContext, command);
        var productCategoryPort = this.domainContext.getCommandPort(ProductCategoryCommandOutboundPort.class);
        var savedProductCategory = productCategoryPort.save(productCategory);
        this.domainEventPublisher.publish(productCategory.getUncommittedEvents());
        return null;
    }
}
