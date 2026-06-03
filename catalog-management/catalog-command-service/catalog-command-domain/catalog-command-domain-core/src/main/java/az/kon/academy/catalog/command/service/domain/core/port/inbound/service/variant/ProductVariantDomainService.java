package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant;

public sealed interface ProductVariantDomainService permits
        ProductVariantManagementDomainService,
        ProductVariantModerationDomainService {

}
