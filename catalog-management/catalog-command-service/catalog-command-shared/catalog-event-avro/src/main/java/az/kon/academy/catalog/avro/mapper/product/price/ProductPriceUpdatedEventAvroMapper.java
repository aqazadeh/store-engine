package az.kon.academy.catalog.avro.mapper.product.price;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.price.ProductPriceUpdatedAvroModel;
import az.kon.academy.catalog.event.product.price.ProductPriceUpdatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductPriceUpdatedEventAvroMapper {

    ProductPriceUpdatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductPriceUpdatedEventAvroMapper.class);

    ProductPriceUpdatedAvroModel toAvro(ProductPriceUpdatedEvent event);

    ProductPriceUpdatedEvent toEvent(ProductPriceUpdatedAvroModel avroModel);
}
