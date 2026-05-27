package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.command.dal.tables.records.ProductCategoryRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryMapper {

    public ProductCategoryRecord toRecord(ProductCategoryRoot root) {
        return new ProductCategoryRecord()
                .setId(root.getRootID().value())
                .setParentId(root.getParent() != null ? root.getParent().value() : null)
                .setName(root.getName().value())
                .setPath(root.getPath().value())
                .setDescription(root.getDescription().value())
                .setImage(root.getImage())
                .setVersion(root.getVersion().value())
                .setProcessStatus(root.getProcessStatus().name())
                .setRowStatus(root.getRowStatus().name())
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime());
    }

    public ProductCategoryRoot toDomain(ProductCategoryRecord r) {
        return ProductCategoryRoot.builder()
                .id(ProductCategoryId.from(r.getId()))
                .parent(r.getParentId() != null ? ProductCategoryId.from(r.getParentId()) : null)
                .name(ProductCategoryName.of(r.getName()))
                .path(ProductCategoryPath.of(r.getPath()))
                .description(ProductCategoryDescription.of(r.getDescription()))
                .image(r.getImage())
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))
                .build();
    }
}
