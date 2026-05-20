package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductRejectedAvroModel;
import az.kon.academy.catalog.event.product.ProductRejectedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductRejectedEventAvroMapper {

    ProductRejectedEventAvroMapper INSTANCE = Mappers.getMapper(ProductRejectedEventAvroMapper.class);

    ProductRejectedAvroModel toAvro(ProductRejectedEvent event);

    ProductRejectedEvent toEvent(ProductRejectedAvroModel avroModel);
}
