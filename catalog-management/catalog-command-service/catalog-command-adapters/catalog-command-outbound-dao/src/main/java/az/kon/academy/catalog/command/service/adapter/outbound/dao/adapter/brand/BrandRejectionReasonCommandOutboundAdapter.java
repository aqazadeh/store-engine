package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandRejectionReasonMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.OptimisticLockingFailureException;

import static az.kon.academy.catalog.command.dal.Tables.BRAND_REJECTION_REASON;

@Slf4j
@CommandAdapter
public class BrandRejectionReasonCommandOutboundAdapter implements BrandRejectionReasonCommandOutboundPort {

    private final DSLContext dsl;
    private final BrandRejectionReasonMapper mapper;

    public BrandRejectionReasonCommandOutboundAdapter(DSLContext dsl, BrandRejectionReasonMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public BrandRejectionReasonRoot save(BrandRejectionReasonRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        var result =  dsl.insertInto(BRAND_REJECTION_REASON)
                .set(record)
                .onConflict(BRAND_REJECTION_REASON.ID)
                .doUpdate()
                .set(record)
                .where(BRAND_REJECTION_REASON.VERSION.eq(aggregate.getVersion().value()))
                .execute();

        if(result == 0){
            log.error("Concurrent update detected for BrandRejectionReason id={}", aggregate.getRootID());
            throw new OptimisticLockingFailureException("Concurrent update detected for BrandRejectionReason id=" + aggregate.getRootID());
        }
        return this.mapper.toDomain(record);
    }
}
