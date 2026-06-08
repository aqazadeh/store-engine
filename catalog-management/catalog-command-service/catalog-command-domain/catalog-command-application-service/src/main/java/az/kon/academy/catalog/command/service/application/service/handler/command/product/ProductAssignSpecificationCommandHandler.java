package az.kon.academy.catalog.command.service.application.service.handler.command.product;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAssignSpecificationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAssignSpecificationsCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general.ProductManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import java.util.List;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Product.PRODUCT_ASSIGN_SPECIFICATION)
public class ProductAssignSpecificationCommandHandler implements AbstractCommandHandler<ProductAssignSpecificationCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final ProductManagementDomainService productManagementDomainService;

    public ProductAssignSpecificationCommandHandler(SeDomainContext domainContext,
                                                    DomainEventPublisher domainEventPublisher,
                                                    ProductManagementDomainService productManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.productManagementDomainService = productManagementDomainService;
    }

    @Override
    public Void handle(ProductAssignSpecificationCommand command) {
        var bulkCommand = ProductAssignSpecificationsCommand.builder()
                .merchantId(command.getMerchantId())
                .productId(command.getProductId())
                .entries(List.of(new ProductAssignSpecificationsCommand.SpecificationEntry(
                        command.getSpecificationId(), command.getValue())))
                .build();
        var aggregate = this.productManagementDomainService.assignSpecifications(domainContext, bulkCommand);
        var port = this.domainContext.getCommandPort(ProductCommandOutboundPort.class);
        port.save(aggregate);
        this.domainEventPublisher.publish(aggregate.getUncommittedEvents());
        return null;
    }
}
