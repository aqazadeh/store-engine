package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantKeyMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantKeyCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.VARIANT_KEY;

@CommandAdapter
public class VariantKeyCommandOutboundAdapter implements VariantKeyCommandOutboundPort {

    private final DSLContext dsl;
    private final VariantKeyMapper mapper;

    public VariantKeyCommandOutboundAdapter(DSLContext dsl, VariantKeyMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

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
