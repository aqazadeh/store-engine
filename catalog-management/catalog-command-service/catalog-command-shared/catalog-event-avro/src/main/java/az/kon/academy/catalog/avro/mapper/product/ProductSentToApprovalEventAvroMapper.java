package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductSentToApprovalAvroModel;
import az.kon.academy.catalog.event.product.ProductSentToApprovalEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductSentToApprovalEventAvroMapper {

    ProductSentToApprovalEventAvroMapper INSTANCE = Mappers.getMapper(ProductSentToApprovalEventAvroMapper.class);

    ProductSentToApprovalAvroModel toAvro(ProductSentToApprovalEvent event);

    ProductSentToApprovalEvent toEvent(ProductSentToApprovalAvroModel avroModel);
}
