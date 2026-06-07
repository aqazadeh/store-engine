package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.BrandRejectionReasonRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import org.springframework.stereotype.Component;

@Component
public class BrandRejectionReasonMapper {

    public BrandRejectionReasonRecord toRecord(BrandRejectionReasonRoot root) {
        return new BrandRejectionReasonRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setBrandId(root.getBrandId().value())
                .setReason(root.getReason())
                .setModeratedBy(root.getModeratedBy().value())
                .setSolved(root.getSolved());
    }

    public BrandRejectionReasonRoot toDomain(BrandRejectionReasonRecord r) {
        return BrandRejectionReasonRoot.builder()
                .id(BrandRejectionReasonId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .brandId(BrandId.from(r.getBrandId()))
                .reason(r.getReason())
                .moderatedBy(ModeratorId.from(r.getModeratedBy()))
                .solved(r.getSolved())
                .build();
    }
}
