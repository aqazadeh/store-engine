package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.tables.records.BrandRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.*;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandRecord toRecord(BrandRoot brand) {
        return new BrandRecord()
                .setId(brand.getRootID().value())
                .setOwnerId(brand.getOwner().value())
                .setName(brand.getName().value())
                .setDescription(brand.getDescription().value())
                .setPath(brand.getPath().value())
                .setImage(brand.getImage())
                .setIsGlobal(brand.getIsGlobal())
                .setStatus(brand.getStatus().name())
                .setVersion(brand.getVersion().value())
                .setProcessStatus(brand.getProcessStatus().name())
                .setRowStatus(brand.getRowStatus().name())
                .setCreationTs(brand.getCreationTs().toOffsetDateTime())
                .setModificationTs(brand.getModificationTs().toOffsetDateTime());
    }

    public BrandRoot toDomain(BrandRecord r) {
        return BrandRoot.builder()
                .id(BrandId.from(r.getId()))
                .owner(MerchantId.from(r.getOwnerId()))
                .name(BrandName.of(r.getName()))
                .description(BrandDescription.of(r.getDescription()))
                .path(BrandPath.of(r.getPath()))
                .image(r.getImage())
                .isGlobal(r.getIsGlobal())
                .status(BrandStatus.valueOf(r.getStatus()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))
                .build();
    }
}
