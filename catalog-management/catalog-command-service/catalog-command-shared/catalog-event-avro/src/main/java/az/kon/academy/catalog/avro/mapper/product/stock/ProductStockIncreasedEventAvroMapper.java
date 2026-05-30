package az.kon.academy.catalog.avro.mapper.product.stock;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.stock.ProductStockIncreasedAvroModel;
import az.kon.academy.catalog.event.product.stock.ProductStockIncreasedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductStockIncreasedEventAvroMapper {

    ProductStockIncreasedEventAvroMapper INSTANCE = Mappers.getMapper(ProductStockIncreasedEventAvroMapper.class);

    ProductStockIncreasedAvroModel toAvro(ProductStockIncreasedEvent event);

    ProductStockIncreasedEvent toEvent(ProductStockIncreasedAvroModel avroModel);
}
