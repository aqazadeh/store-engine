package az.kon.academy.catalog.avro.mapper.product.stock;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.stock.ProductStockDecreasedAvroModel;
import az.kon.academy.catalog.event.product.stock.ProductStockDecreasedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductStockDecreasedEventAvroMapper {

    ProductStockDecreasedEventAvroMapper INSTANCE = Mappers.getMapper(ProductStockDecreasedEventAvroMapper.class);

    ProductStockDecreasedAvroModel toAvro(ProductStockDecreasedEvent event);

    ProductStockDecreasedEvent toEvent(ProductStockDecreasedAvroModel avroModel);
}
