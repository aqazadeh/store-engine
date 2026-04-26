package az.kon.academy.catalog.command.service.domain.core.command.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class SpecificationChangeInformationCommand {
    @Getter private final ProductSpecificationId productSpecificationId;
    @Getter private final SpecificationName name;
    @Getter private final SpecificationDescription description;
}
