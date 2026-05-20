package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductCreatedAvroModel;
import az.kon.academy.catalog.event.product.ProductCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductCreatedEventAvroMapper {

    ProductCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductCreatedEventAvroMapper.class);

    ProductCreatedAvroModel toAvro(ProductCreatedEvent event);

    ProductCreatedEvent toEvent(ProductCreatedAvroModel avroModel);
}
