package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeOwnerCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandRejectCommand;

public interface BrandManagementDomainService {

    BrandRoot createGlobalBrand(DomainContext context, BrandCreateCommand command);

    BrandRoot changeBrandOwner(DomainContext context, BrandChangeOwnerCommand command);

    BrandRoot approve(DomainContext context, BrandApproveCommand command);

    BrandRoot reject(DomainContext context, BrandRejectCommand command);
}
