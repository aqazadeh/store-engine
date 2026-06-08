package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.OptimisticLockingFailureException;


import static az.kon.academy.catalog.sql.dal.Tables.BRAND;

@Slf4j
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
        var result = dsl.insertInto(BRAND)
                .set(record)
                .onConflict(BRAND.ID)
                .doUpdate()
                .set(record)
                .where(BRAND.VERSION.eq(aggregate.getVersion().value()))
                .execute();
        if(result == 0){
            log.error("Concurrent update detected for Brand id={}", aggregate.getRootID());
            throw new OptimisticLockingFailureException("Concurrent update detected for Brand id=" + aggregate.getRootID());
        }
        return this.mapper.toDomain(record);
    }
}
