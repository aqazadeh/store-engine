package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandApproveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandRejectCommand;

public interface BrandManagementDomainService {
    BrandRoot approve(BrandApproveCommand command);

    BrandRoot reject(BrandRejectCommand command);
}
