package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductApprovedAvroModel;
import az.kon.academy.catalog.event.product.ProductApprovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductApprovedEventAvroMapper {

    ProductApprovedEventAvroMapper INSTANCE = Mappers.getMapper(ProductApprovedEventAvroMapper.class);

    ProductApprovedAvroModel toAvro(ProductApprovedEvent event);

    ProductApprovedEvent toEvent(ProductApprovedAvroModel avroModel);
}
