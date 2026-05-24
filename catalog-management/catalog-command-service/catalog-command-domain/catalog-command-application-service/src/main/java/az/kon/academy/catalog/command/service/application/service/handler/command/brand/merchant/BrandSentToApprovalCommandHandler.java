package az.kon.academy.catalog.command.service.application.service.handler.command.brand.merchant;

import az.kon.academy.application.core.annotation.CommandHandler;
import az.kon.academy.catalog.command.service.application.service.constant.SecurityPermissions;
import az.kon.academy.catalog.command.service.application.service.handler.AbstractCommandHandler;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.BrandSentToApprovalCommand;
import az.kon.academy.catalog.command.service.domain.core.service.brand.BrandModificationDomainService;
import az.kon.academy.domain.core.SeDomainContext;
import az.kon.academy.event.handler.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Qualifier;

@CommandHandler(
        roles = SecurityPermissions.Role.ROLE_MERCHANT,
        permissions = SecurityPermissions.Brand.BRAND_MERCHANT_MOVE_DRAFT)
public class BrandSentToApprovalCommandHandler implements AbstractCommandHandler<BrandSentToApprovalCommand, Void> {

    private final SeDomainContext domainContext;
    private final DomainEventPublisher domainEventPublisher;
    private final BrandModificationDomainService brandModificationDomainService;

    public BrandSentToApprovalCommandHandler(SeDomainContext domainContext,
                                             DomainEventPublisher domainEventPublisher,
                                             @Qualifier("brandModificationDomainService") BrandModificationDomainService brandModificationDomainService) {
        this.domainContext = domainContext;
        this.domainEventPublisher = domainEventPublisher;
        this.brandModificationDomainService = brandModificationDomainService;
    }

    @Override
    public Void handle(BrandSentToApprovalCommand command) {
        var brandCommandPort = this.domainContext.getCommandPort(BrandCommandPort.class);
        var brand = this.brandModificationDomainService.sentToApproval(domainContext, command);
        var savedBrand = brandCommandPort.save(brand);
        this.domainEventPublisher.publish(brand.getUncommittedEvents());
        return null;
    }
}
