package az.kon.academy.catalog.command.service.application.service.handler.command.brand.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand.BrandManagementDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_DOMAIN_MODERATOR,
        permissions = SecurityPermissions.Brand.BRAND_MANAGEMENT_REJECT)
public class BrandRejectCommandHandler implements AbstractCommandHandler<BrandCreateRejectionReasonCommand, Void> {
    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandManagementDomainService brandManagementDomainService;

    public BrandRejectCommandHandler(SeDomainContext domainContext,
                                     DomainEventPublisher domainEventPublisher,
                                     @Qualifier("brandManagementDomainService") BrandManagementDomainService brandManagementDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandManagementDomainService = brandManagementDomainService;
    }

    @Override
    public Void handle(BrandCreateRejectionReasonCommand command) {
        var brandRejectionReason = this.brandManagementDomainService.rejectReason(domainContext, command);
        var brandRejectionCommandPort = this.domainContext.getCommandPort(BrandRejectionReasonCommandPort.class);
        var savedReason = brandRejectionCommandPort.save(brandRejectionReason);
        this.domainEventPublisher.publish(brandRejectionReason.getUncommittedEvents());
        return null;
    }

}
