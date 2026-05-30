package az.kon.academy.catalog.command.service.application.service.handler.command.brandrejection.management;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.application.core.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection.BrandRejectionManagementService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Brand.BRAND_MANAGEMENT_SOLVE_REJECT_REASON)
public class BrandRejectionReasonSolveCommandHandler implements AbstractCommandHandler<BrandRejectionReasonSolveCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandRejectionManagementService brandRejectionManagementService;

    public BrandRejectionReasonSolveCommandHandler(SeDomainContext domainContext,
                                                   DomainEventPublisher domainEventPublisher,
                                                   BrandRejectionManagementService brandRejectionManagementService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandRejectionManagementService = brandRejectionManagementService;
    }

    @Override
    public Void handle(BrandRejectionReasonSolveCommand command) {
        var brandRejectionReasonCommandPort = this.domainContext.getCommandPort(BrandRejectionReasonCommandOutboundPort.class);
        var brandRejectionReason = this.brandRejectionManagementService.solve(domainContext, command);
        var savedBrandRejectionReason = brandRejectionReasonCommandPort.save(brandRejectionReason);
        this.domainEventPublisher.publish(brandRejectionReason.getUncommittedEvents());
        return null;
    }
}
