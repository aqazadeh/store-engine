package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandSentToApprovalCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Brand.BRAND_MANAGEMENT_SENT_TO_APPROVAL;
import static az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions.Role.ROLE_MERCHANT;

@CommandHandler(roles = ROLE_MERCHANT, permissions = BRAND_MANAGEMENT_SENT_TO_APPROVAL)
public class BrandSentToApprovalCommandHandler implements AbstractCommandHandler<BrandSentToApprovalCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandSentToApprovalCommandHandler(SeDomainContext domainContext,
                                             DomainEventPublisher domainEventPublisher,
                                             BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public Void handle(BrandSentToApprovalCommand command) {
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandOutboundPort.class);
        var brand = this.brandManagementDomainService.sentToApproval(domainContext, command);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}
