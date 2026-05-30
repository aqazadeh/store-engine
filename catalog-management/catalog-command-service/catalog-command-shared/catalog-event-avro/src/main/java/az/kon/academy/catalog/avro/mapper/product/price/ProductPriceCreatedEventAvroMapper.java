package az.kon.academy.catalog.avro.mapper.product.price;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.price.ProductPriceCreatedAvroModel;
import az.kon.academy.catalog.event.product.price.ProductPriceCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductPriceCreatedEventAvroMapper {

    ProductPriceCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductPriceCreatedEventAvroMapper.class);

    ProductPriceCreatedAvroModel toAvro(ProductPriceCreatedEvent event);

    ProductPriceCreatedEvent toEvent(ProductPriceCreatedAvroModel avroModel);
}
