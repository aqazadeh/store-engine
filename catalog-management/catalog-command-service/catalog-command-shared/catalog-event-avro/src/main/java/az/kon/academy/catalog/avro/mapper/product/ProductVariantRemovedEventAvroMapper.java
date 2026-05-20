package az.kon.academy.catalog.avro.mapper.product;

import az.kon.academy.catalog.avro.common.AvroMapStructConfig;
import az.kon.academy.catalog.avro.model.product.ProductVariantRemovedAvroModel;
import az.kon.academy.catalog.event.product.ProductVariantRemovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "default", config = AvroMapStructConfig.class)
public interface ProductVariantRemovedEventAvroMapper {

    ProductVariantRemovedEventAvroMapper INSTANCE = Mappers.getMapper(ProductVariantRemovedEventAvroMapper.class);

    ProductVariantRemovedAvroModel toAvro(ProductVariantRemovedEvent event);

    ProductVariantRemovedEvent toEvent(ProductVariantRemovedAvroModel avroModel);
}
