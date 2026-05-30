package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.enums.BrandStatusType;
import az.kon.academy.catalog.command.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.BrandRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandRecord toRecord(BrandRoot root) {
        return new BrandRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setOwnerId(root.getOwner().value())
                .setName(root.getName().value())
                .setDescription(root.getDescription().value())
                .setPath(root.getPath().value())
                .setImage(root.getImage())
                .setIsGlobal(root.getIsGlobal())
                .setStatus(BrandStatusType.valueOf(root.getStatus().name()));
    }

    public BrandRoot toDomain(BrandRecord r) {
        return BrandRoot.builder()
                .id(BrandId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .owner(MerchantId.from(r.getOwnerId()))
                .name(BrandName.of(r.getName()))
                .description(BrandDescription.of(r.getDescription()))
                .path(BrandPath.of(r.getPath()))
                .image(r.getImage())
                .isGlobal(r.getIsGlobal())
                .status(BrandStatus.valueOf(r.getStatus().name()))
                .build();
    }
}
