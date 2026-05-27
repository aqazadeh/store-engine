package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantKeyMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantKeyCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.VARIANT_KEY;

@Component
@RequiredArgsConstructor
public class VariantKeyCommandAdapter implements VariantKeyCommandPort {

    private final DSLContext dsl;
    private final VariantKeyMapper mapper;

    @Override
    public VariantKeyRoot save(VariantKeyRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(VARIANT_KEY)
                .set(record)
                .onConflict(VARIANT_KEY.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
