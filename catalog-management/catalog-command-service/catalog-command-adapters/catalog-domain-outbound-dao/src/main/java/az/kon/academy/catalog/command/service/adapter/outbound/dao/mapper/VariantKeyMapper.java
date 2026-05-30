package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.VariantKeyRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import org.springframework.stereotype.Component;

@Component
public class VariantKeyMapper {

    public VariantKeyRecord toRecord(VariantKeyRoot root) {
        return new VariantKeyRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setName(root.getName().value())
                .setDescription(root.getDescription());
    }

    public VariantKeyRoot toDomain(VariantKeyRecord r) {
        return VariantKeyRoot.builder()
                .id(VariantKeyId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .name(VariantName.of(r.getName()))
                .description(r.getDescription())
                .build();
    }
}
