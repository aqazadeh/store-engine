package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantValueMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.VariantValueQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.VARIANT_VALUE;

@QueryAdapter
public class VariantValueQueryAdapter implements VariantValueQueryPort {

    private final DSLContext dsl;
    private final VariantValueMapper mapper;

    public VariantValueQueryAdapter(DSLContext dsl, VariantValueMapper mapper) {
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
}
