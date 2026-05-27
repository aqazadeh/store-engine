package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.BRAND;

@Component
@RequiredArgsConstructor
public class BrandCommandAdapter implements BrandCommandPort {

    private final DSLContext dsl;
    private final BrandMapper mapper;

    @Override
    public BrandRoot save(BrandRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(BRAND)
                .set(record)
                .onConflict(BRAND.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
