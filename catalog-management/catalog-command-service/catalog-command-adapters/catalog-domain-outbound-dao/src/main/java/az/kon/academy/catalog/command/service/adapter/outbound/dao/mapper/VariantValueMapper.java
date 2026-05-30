package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.VariantValueRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import org.springframework.stereotype.Component;

@Component
public class VariantValueMapper {

    public VariantValueRecord toRecord(VariantValueRoot root) {
        return new VariantValueRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setKeyId(root.getKeyId().value())
                .setName(root.getName().value());
    }

    public VariantValueRoot toDomain(VariantValueRecord r) {
        return VariantValueRoot.builder()
                .id(VariantValueId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .keyId(VariantKeyId.from(r.getKeyId()))
                .name(VariantValue.of(r.getName()))
                .build();
    }
}
