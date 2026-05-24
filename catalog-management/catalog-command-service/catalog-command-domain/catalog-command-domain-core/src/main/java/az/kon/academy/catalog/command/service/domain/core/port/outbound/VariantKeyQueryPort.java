package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;

import java.util.List;
import java.util.Optional;

public interface VariantKeyQueryPort {

    Optional<VariantKeyRoot> findById(VariantKeyId id);

    default VariantKeyRoot fetchById(VariantKeyId id) {
        return this.findById(id)
                .orElseThrow(() -> new VariantEntityNotFoundException(
                        VariantDomainErrorCodes.KEY_NOT_FOUND,
                        List.of(id.value().toString())));
    }
}