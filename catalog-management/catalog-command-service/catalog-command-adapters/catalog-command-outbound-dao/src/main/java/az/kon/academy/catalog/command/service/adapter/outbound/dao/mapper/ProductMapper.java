package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.ProcessStatus;
import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.aggragate.valueobject.Version;
import az.kon.academy.catalog.sql.dal.enums.ProcessStatusType;
import az.kon.academy.catalog.sql.dal.enums.ProductStatusType;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.sql.dal.tables.records.ProductRecord;
import az.kon.academy.catalog.sql.dal.tables.records.ProductSpecificationAssignmentRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductRecord toRecord(ProductRoot root) {
        return new ProductRecord()
                .setId(root.getRootID().value())
                .setVersion(root.getVersion().value())
                .setProcessStatus(ProcessStatusType.valueOf(root.getProcessStatus().name()))
                .setRowStatus(RowStatusType.valueOf(root.getRowStatus().name()))
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setMerchantId(root.getMerchantId().value())
                .setCategoryId(root.getCategoryId() != null ? root.getCategoryId().value() : null)
                .setBrandId(root.getBrandId() != null ? root.getBrandId().value() : null)
                .setName(root.getName().value())
                .setDescription(root.getDescription().value())
                .setStatus(ProductStatusType.valueOf(root.getStatus().name()));
    }

    public ProductSpecificationAssignmentRecord toSpecAssignmentRecord(
            ProductRoot root, ProductSpecificationAssignment assignment) {
        return new ProductSpecificationAssignmentRecord()
                .setProductId(root.getRootID().value())
                .setSpecificationId(assignment.getSpecificationId().value())
                .setValue(assignment.getValue().value());
    }

    public ProductRoot toDomain(
            ProductRecord r,
            List<ProductSpecificationAssignmentRecord> specs,
            List<ProductVariantRoot> variants) {

        List<ProductSpecificationAssignment> specAssignments = specs.stream()
                .map(s -> ProductSpecificationAssignment.of(
                        ProductSpecificationId.from(s.getSpecificationId()),
                        ProductSpecificationValue.of(s.getValue())
                ))
                .toList();

        return ProductRoot.builder()
                .id(ProductId.from(r.getId()))
                .version(Version.of(r.getVersion()))
                .processStatus(ProcessStatus.valueOf(r.getProcessStatus().name()))
                .rowStatus(RowStatus.valueOf(r.getRowStatus().name()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .merchantId(MerchantId.from(r.getMerchantId()))
                .categoryId(r.getCategoryId() != null ? ProductCategoryId.from(r.getCategoryId()) : null)
                .brandId(r.getBrandId() != null ? BrandId.from(r.getBrandId()) : null)
                .name(ProductName.of(r.getName()))
                .description(ProductDescription.of(r.getDescription()))
                .status(ProductStatus.valueOf(r.getStatus().name()))
                .specifications(specAssignments)
                .build();
    }
}
