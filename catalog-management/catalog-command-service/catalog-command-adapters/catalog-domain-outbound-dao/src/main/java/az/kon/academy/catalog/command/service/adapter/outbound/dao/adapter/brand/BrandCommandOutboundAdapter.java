package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.BRAND;

@CommandAdapter
public class BrandCommandOutboundAdapter implements BrandCommandOutboundPort {

    private final DSLContext dsl;
    private final BrandMapper mapper;

    public BrandCommandOutboundAdapter(DSLContext dsl, BrandMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public BrandRoot save(BrandRoot aggregate) {
        var record = mapper.toRecord(aggregate.increaseVersion());
        dsl.insertInto(BRAND)
                .set(record)
                .onConflict(BRAND.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
