package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandRejectionReasonMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.BrandRejectionReasonCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.BRAND_REJECTION_REASON;

@CommandAdapter
public class BrandRejectionReasonCommandAdapter implements BrandRejectionReasonCommandPort {

    private final DSLContext dsl;
    private final BrandRejectionReasonMapper mapper;

    public BrandRejectionReasonCommandAdapter(DSLContext dsl, BrandRejectionReasonMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public BrandRejectionReasonRoot save(BrandRejectionReasonRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        var effectedRows =  dsl.insertInto(BRAND_REJECTION_REASON)
                .set(record)
                .onConflict(BRAND_REJECTION_REASON.ID)
                .doUpdate()
                .set(record)
                .execute();

        if(effectedRows == 0) {
            throw new RuntimeException("insert or update not worked"); //FIXME
        }
        return aggregate;
    }
}
