package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductInformationChangedAvroModel;
import az.kon.academy.catalog.event.product.ProductInformationChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductInformationChangedEventAvroMapper {

    ProductInformationChangedEventAvroMapper INSTANCE = Mappers.getMapper(ProductInformationChangedEventAvroMapper.class);

    ProductInformationChangedAvroModel toAvro(ProductInformationChangedEvent event);

    ProductInformationChangedEvent toEvent(ProductInformationChangedAvroModel avroModel);
}
