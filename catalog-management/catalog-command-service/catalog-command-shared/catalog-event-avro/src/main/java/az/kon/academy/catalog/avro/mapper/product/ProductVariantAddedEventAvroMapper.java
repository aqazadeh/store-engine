package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductVariantAddedAvroModel;
import az.kon.academy.catalog.event.product.ProductVariantAddedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductVariantAddedEventAvroMapper {

    ProductVariantAddedEventAvroMapper INSTANCE = Mappers.getMapper(ProductVariantAddedEventAvroMapper.class);

    ProductVariantAddedAvroModel toAvro(ProductVariantAddedEvent event);

    ProductVariantAddedEvent toEvent(ProductVariantAddedAvroModel avroModel);
}
