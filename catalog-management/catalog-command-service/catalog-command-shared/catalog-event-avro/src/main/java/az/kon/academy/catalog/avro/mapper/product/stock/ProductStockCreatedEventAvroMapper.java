package az.kon.academy.catalog.avro.mapper.product.stock;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.stock.ProductStockCreatedAvroModel;
import az.kon.academy.catalog.event.product.stock.ProductStockCreatedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductStockCreatedEventAvroMapper {

    ProductStockCreatedEventAvroMapper INSTANCE = Mappers.getMapper(ProductStockCreatedEventAvroMapper.class);

    ProductStockCreatedAvroModel toAvro(ProductStockCreatedEvent event);

    ProductStockCreatedEvent toEvent(ProductStockCreatedAvroModel avroModel);
}
