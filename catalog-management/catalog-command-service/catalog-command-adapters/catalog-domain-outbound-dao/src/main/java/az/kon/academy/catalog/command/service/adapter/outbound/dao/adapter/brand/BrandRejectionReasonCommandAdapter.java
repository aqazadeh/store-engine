package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandRejectionReasonMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.BRAND_REJECTION_REASON;

@Component
@RequiredArgsConstructor
public class BrandRejectionReasonCommandAdapter implements BrandRejectionReasonCommandPort {

    private final DSLContext dsl;
    private final BrandRejectionReasonMapper mapper;

    @Override
    public BrandRejectionReasonRoot save(BrandRejectionReasonRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(BRAND_REJECTION_REASON)
                .set(record)
                .onConflict(BRAND_REJECTION_REASON.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
