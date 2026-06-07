package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.variant;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.VariantValueMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.VariantValueCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.VARIANT_VALUE;

@CommandAdapter
public class VariantValueCommandOutboundAdapter implements VariantValueCommandOutboundPort {

    private final DSLContext dsl;
    private final VariantValueMapper mapper;

    public VariantValueCommandOutboundAdapter(DSLContext dsl, VariantValueMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public VariantValueRoot save(VariantValueRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(VARIANT_VALUE)
                .set(record)
                .onConflict(VARIANT_VALUE.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
