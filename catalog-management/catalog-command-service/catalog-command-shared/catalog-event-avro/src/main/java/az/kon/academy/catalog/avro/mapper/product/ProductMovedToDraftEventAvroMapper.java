package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductMovedToDraftAvroModel;
import az.kon.academy.catalog.event.product.ProductMovedToDraftEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductMovedToDraftEventAvroMapper {

    ProductMovedToDraftEventAvroMapper INSTANCE = Mappers.getMapper(ProductMovedToDraftEventAvroMapper.class);

    ProductMovedToDraftAvroModel toAvro(ProductMovedToDraftEvent event);

    ProductMovedToDraftEvent toEvent(ProductMovedToDraftAvroModel avroModel);
}
