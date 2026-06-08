package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.aggragate.BaseRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProductVariantKeyQueryOutboundPort extends BaseQueryPort {

    Optional<VariantKeyRoot> findById(VariantKeyId id);

    VariantKeyRoot fetchById(VariantKeyId id);

}