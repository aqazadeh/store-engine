package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateForGlobalCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.management.BrandRejectCommand;
import az.kon.academy.domain.core.SeDomainContext;

public interface BrandManagementDomainService {

    BrandRoot createGlobalBrand(SeDomainContext context, BrandCreateForGlobalCommand command);

    BrandRoot changeBrandOwner(SeDomainContext context, BrandChangeOwnerCommand command);

    BrandRoot approve(SeDomainContext context, BrandApproveCommand command);

    BrandRoot reject(SeDomainContext context, BrandRejectCommand command);

    BrandRejectionReasonRoot rejectReason(SeDomainContext context, BrandCreateRejectionReasonCommand command);
}
