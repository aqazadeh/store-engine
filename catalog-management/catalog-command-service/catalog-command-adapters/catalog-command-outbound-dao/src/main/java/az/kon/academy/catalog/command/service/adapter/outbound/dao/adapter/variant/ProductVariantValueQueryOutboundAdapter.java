package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantValueMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.variant.VariantEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantValueQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.VARIANT_VALUE;

@QueryAdapter
public class ProductVariantValueQueryOutboundAdapter implements ProductVariantValueQueryOutboundPort {

    private final DSLContext dsl;
    private final VariantValueMapper mapper;

    public ProductVariantValueQueryOutboundAdapter(DSLContext dsl, VariantValueMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<VariantValueRoot> findById(VariantValueId id) {
        return dsl.selectFrom(VARIANT_VALUE)
                .where(VARIANT_VALUE.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public VariantValueRoot fetchById(VariantValueId id) {
        return this.findById(id)
                .orElseThrow(() -> new VariantEntityNotFoundException(
                        VariantDomainErrorCodes.VALUE_NOT_FOUND, List.of(id.toString())));
    }

    @Override
    public void checkAllAssignmentsExist(Map<VariantKeyId, List<VariantValueId>> assignmentMap) {
        for (var entry : assignmentMap.entrySet()) {
            var keyId = entry.getKey().value();
            var valueIds = entry.getValue().stream().map(VariantValueId::value).toList();
            if (valueIds.isEmpty()) {
                continue;
            }
            var foundCount = dsl.selectCount()
                    .from(VARIANT_VALUE)
                    .where(VARIANT_VALUE.KEY_ID.eq(keyId)
                            .and(VARIANT_VALUE.ID.in(valueIds)))
                    .fetchOne(0, Integer.class);
            if (foundCount != valueIds.size()) {
                throw new VariantEntityNotFoundException(VariantDomainErrorCodes.VALUE_NOT_FOUND,
                        List.of("key=" + entry.getKey() + " values=" + valueIds));
            }
        }
    }
}
