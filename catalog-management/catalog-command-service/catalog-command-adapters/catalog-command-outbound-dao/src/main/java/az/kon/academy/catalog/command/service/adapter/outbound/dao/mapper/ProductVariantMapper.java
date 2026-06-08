package az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper;

import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantAssignmentRecord;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantRecord;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductVariantMapper {

    public ProductVariantRecord toRecord(ProductVariantRoot root, UUID productId) {
        return new ProductVariantRecord()
                .setId(root.getRootID().value())
                .setCreationTs(root.getCreationTs().toOffsetDateTime())
                .setModificationTs(root.getModificationTs().toOffsetDateTime())

                .setProductId(productId);
    }

    public ProductVariantAssignmentRecord toAssignmentRecord(UUID variantId, ProductVariantAssignment assignment) {
        return new ProductVariantAssignmentRecord()
                .setVariantId(variantId)
                .setVariantKeyId(assignment.getVariantKeyId().value())
                .setVariantValueId(assignment.getVariantValueId().value());
    }

    public ProductVariantRoot toDomain(ProductVariantRecord r, List<ProductVariantAssignmentRecord> assignments) {
        List<ProductVariantAssignment> domainAssignments = assignments.stream()
                .map(a -> ProductVariantAssignment.of(
                        VariantKeyId.from(a.getVariantKeyId()),
                        VariantValueId.from(a.getVariantValueId())
                ))
                .toList();

        return ProductVariantRoot.builder()
                .id(ProductVariantId.from(r.getId()))
                .creationTs(SeDateTime.of(r.getCreationTs()))
                .modificationTs(SeDateTime.of(r.getModificationTs()))

                .assignments(domainAssignments)
                .build();
    }
}
