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

    List<VariantKeyRoot> findAllById(List<VariantKeyId> ids);

    default VariantKeyRoot fetchById(VariantKeyId id) {
        return this.findById(id)
                .orElseThrow(() -> new VariantEntityNotFoundException(
                        VariantDomainErrorCodes.KEY_NOT_FOUND,
                        List.of(id.value().toString())));
    }

    default void checkAllKeysExist(List<VariantKeyId> ids) {
        var found = this.findAllById(ids);
        if (found.size() != ids.size()) {
            var foundIds = found.stream().map(BaseRoot::getRootID).collect(Collectors.toSet());
            var missing = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .map(id -> id.value().toString())
                    .collect(Collectors.joining(", "));
            throw new VariantEntityNotFoundException(
                    VariantDomainErrorCodes.KEY_NOT_FOUND,
                    List.of(missing));
        }
    }
}