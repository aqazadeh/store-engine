package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductVariantValueQueryOutboundPort extends BaseQueryPort {

    Optional<VariantValueRoot> findById(VariantValueId id);

    List<VariantValueRoot> findAllByKeyValueMap(Map<VariantKeyId, List<VariantValueId>> assignmentMap);

    default VariantValueRoot fetchById(VariantValueId id) {
        return this.findById(id)
                .orElseThrow(() -> new VariantEntityNotFoundException(
                        VariantDomainErrorCodes.VALUE_NOT_FOUND,
                        List.of(id.value().toString())));
    }

    default void checkAllAssignmentsExist(Map<VariantKeyId, List<VariantValueId>> assignmentMap) {
        var found = this.findAllByKeyValueMap(assignmentMap);
        int expected = assignmentMap.values().stream().mapToInt(List::size).sum();
        if (found.size() != expected) {
            throw new VariantEntityNotFoundException(
                    VariantDomainErrorCodes.VALUE_NOT_FOUND,
                    List.of("some variant assignments not found"));
        }
    }
}