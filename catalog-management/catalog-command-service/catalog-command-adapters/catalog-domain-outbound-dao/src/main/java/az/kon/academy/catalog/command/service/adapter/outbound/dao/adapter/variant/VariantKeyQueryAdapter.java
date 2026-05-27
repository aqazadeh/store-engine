package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantKeyMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.VariantKeyQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.VARIANT_KEY;

@Component
@RequiredArgsConstructor
public class VariantKeyQueryAdapter implements VariantKeyQueryPort {

    private final DSLContext dsl;
    private final VariantKeyMapper mapper;

    @Override
    public Optional<VariantKeyRoot> findById(VariantKeyId id) {
        return dsl.selectFrom(VARIANT_KEY)
                .where(VARIANT_KEY.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
